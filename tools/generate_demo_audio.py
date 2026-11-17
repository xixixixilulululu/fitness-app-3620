"""Generate three short original WAV rhythms for the class prototype.

The output is intentionally simple and copyright-free. Run this script again if
the binary resources need to be recreated.
"""

import math
import struct
import wave
from pathlib import Path


OUTPUT_DIR = Path(__file__).resolve().parents[1] / "app" / "src" / "main" / "res" / "raw"
SAMPLE_RATE = 8_000
DURATION_SECONDS = 8.0
TRACKS = (
    ("focus_demo", 90, 220),
    ("energy_demo", 125, 330),
    ("power_demo", 145, 440),
)


def create_track(name: str, bpm: int, frequency: int) -> None:
    beat_seconds = 60.0 / bpm
    frames = []
    total_frames = int(SAMPLE_RATE * DURATION_SECONDS)

    for index in range(total_frames):
        time_seconds = index / SAMPLE_RATE
        beat_phase = time_seconds % beat_seconds
        envelope = max(0.0, 1.0 - beat_phase / 0.11) if beat_phase < 0.11 else 0.0
        low_envelope = max(0.0, 1.0 - beat_phase / 0.18) if beat_phase < 0.18 else 0.0
        tone = math.sin(2 * math.pi * frequency * time_seconds) * envelope
        accent = 0.35 * math.sin(2 * math.pi * (frequency / 2) * time_seconds) * low_envelope
        sample = int(max(-1.0, min(1.0, (tone + accent) * 0.32)) * 32_767)
        frames.append(struct.pack("<h", sample))

    output_path = OUTPUT_DIR / f"{name}.wav"
    with wave.open(str(output_path), "wb") as output:
        output.setnchannels(1)
        output.setsampwidth(2)
        output.setframerate(SAMPLE_RATE)
        output.writeframes(b"".join(frames))


def main() -> None:
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    for track_name, bpm, frequency in TRACKS:
        create_track(track_name, bpm, frequency)


if __name__ == "__main__":
    main()
