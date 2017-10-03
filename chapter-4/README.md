## Talent Search Engine
A talent search application to search developers across a city for a technology.

The aim of this application is to achieve two things:
* Learn more about Play by using its API: ActionBuilders, WSClient, configurations etc
* Understand the difficulties faces in such architectures. So that we are better prepared to address these problems going forward in
other chapters.

### Run
To run the code: `sbt runAll`

And then connect to `http://localhost:3000`. After logging in, you may search by entering: 
Scala developers in London


### Notes
* If the program results in `OutofMemoryError` when running with `runAll` command, provide the memory flag: `sbt -mem 2000 runAll`. In this case, it means 2000mb of ram.
* On Intellij, in case you are unable to load the project, then refer: http://stackoverflow.com/questions/41966066/jboss-interceptor-api-1-1-not-found-when-added-as-sbt-dependency
