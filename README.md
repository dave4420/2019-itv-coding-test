# Dave Hinton’s coding test for ITV

## Building

- `sbt assembly`

Runs tests and builds `target/scala-2.13/dave-hinton-2019-itv-coding-test-assembly-0.1.jar`.

## Running

Note that `ffmpeg` must be on the path. 

- `bin/run.sh` *input-file-name* *output-file-name* *media-id* *thumbnail-offset*
    - *input-file-name* — path name to the video file from which a thumbnail is to be extracted; the filename must
        match the filename in the original download from the Internet Archive
    - *output-file-name* — path name to write the thumbnail to
    - *media-id* — identifies the media on the Internet Archive
    - *thumbnail-offset* — how far into the video to take the thumbnail from; must be in
        [ISO 8601 duration format](https://en.wikipedia.org/wiki/ISO_8601#Durations), e.g. `PT1.5S` to take the
        thumbnail from 1.5 seconds into the video file

## Assumptions

- we always want to verify that video has not been modified

## Design Decisions

- using akka-http to access the Internet Archive only because it's what we use at my current role, so I'm familiar with
    it
    - and spray to parse JSON, for the same reason

## Technical Debt

- some parameters (e.g. timeouts) are hardcoded and cannot currently be overridden from the command line
- there are no tests for parsing the command line because it is currently very simple; if we added optional switches,
    we should add tests for it (we should also start using a proper command line parser in this case)
- I'm using `Await.result` in `IntegrityChecker` (should really return the future and deal with it in `Main`)
- no integration tests (because I didn't want to spend the time stubbing out the Internet Archive API)
