# Java libraries for Music Generation

## Table of Contents
- [Overview](#overview)
- [Installation](#installation)
- [Usage](#usage)
- [Main](#main)
- [Music](#music)
- [Progression Generator](#progression-generator)
- [Sound Player](#sound-player)
- [Grader](#grader)

## Overview
This Java project contains many packages to aid in music generation. The current packages are:
- main
- music
- progressionGenerator
- soundPlayer
- grader

## Installation

## Usage

## Music
This package contains classes to be used for representing common musical elements such as notes, chords, etc. The Rhythm features are still in development. The Note and Chord classes are used to store the actual notes and chords, while the PlayedNote and PlayedChord classes contain information about the duration and volume at which they should be played. Currently, the Key class contains support for Major, Natural Minor, and Harmonic Minor keys. The Range class is used to represent a range of notes, useful for ensuring that a Note is within the range of an instrument or voice.

### Classes
- Note
- PlayedNote
- Chord
- PlayedChord
- Key
- Range
- Rhythm (in development)

### Enums
- Tone
- Shift
- RhythmNote (in development)

## Progression Generator
This package is used to generate (major) chord progressions. It uses a Markov Chain and standard chord structures to generate these progressions. This package does not yet contain support for inverted chords or secondary dominance.

### Classes
- ChordNode
- ProgressionGenerator

## Sound Player
This package is used to actually play the musical elements from the [Music](#music) package. It utilizes the `javax.sound.midi` package to convert notes into MIDI sounds. It also contains functionality for playing notes with whichever MIDI instruments are available on the machine. This package does not yet contain functionality for playing multiple separate musical lines at once, although chords can be played.

### Classes
- SoundPlayer

## Grader
This package contains the mechanism for determining the fitness of a particular four-part harmony with respect to itself, as well as the chord progression from which it was formed. Currently, it checks for the absence of voice mixing, parallel octaves, and parallel fifths. This package is to be used later in the genetic algorithm package to determine the organisms that will be allowed to reproduce.

### Classes
- Grader
