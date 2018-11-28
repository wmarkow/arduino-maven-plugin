Here it is explained

https://vzurczak.wordpress.com/2014/07/23/another-cryptic-message-from-maven/

how to fix this error.

What matters here, is…

    role: org.apache.maven.plugin.Mojo
    roleHint: net.roboconf:roboconf-maven-plugin:1.0-SNAPSHOT:validate

What happened with my plug-in is that I had cleaned my Maven project.
After executing mvn clean test -X, I noticed the maven-plugin-plugin generates a descriptor from my mojos.
The org.apache.maven.plugin.Mojo role is generated during this step. The role-hint is generated from my mojos. When I cleaned my project, I simply deleted the descriptor, which resulted in the error above.

After a refresh in Eclipse, I could go farther in my unit tests.
Conclusion: if you want to run the unit tests of your Maven plug-in in Eclipse, 
make sure it was compiled with Maven first.


org.codehaus.plexus.component.repository.exception.ComponentLookupException: java.util.NoSuchElementException
      role: org.apache.maven.plugin.Mojo
  roleHint: com.github.wmarkow.amp:arduino-maven-plugin:1.0.0-SNAPSHOT:download-dependencies
    at org.codehaus.plexus.DefaultPlexusContainer.lookup(DefaultPlexusContainer.java:267)
    at org.codehaus.plexus.DefaultPlexusContainer.lookup(DefaultPlexusContainer.java:243)
    at org.codehaus.plexus.PlexusTestCase.lookup(PlexusTestCase.java:205)
    at org.apache.maven.plugin.testing.AbstractMojoTestCase.lookupMojo(AbstractMojoTestCase.java:410)
    at org.apache.maven.plugin.testing.AbstractMojoTestCase.lookupMojo(AbstractMojoTestCase.java:355)
    at com.github.wmarkow.amp.mojo.FetchDependenciesMojoTest.testMojoGoal(FetchDependenciesMojoTest.java:30)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:498)
    at junit.framework.TestCase.runTest(TestCase.java:176)
    at junit.framework.TestCase.runBare(TestCase.java:141)
    at junit.framework.TestResult$1.protect(TestResult.java:122)
    at junit.framework.TestResult.runProtected(TestResult.java:142)
    at junit.framework.TestResult.run(TestResult.java:125)
    at junit.framework.TestCase.run(TestCase.java:129)
    at junit.framework.TestSuite.runTest(TestSuite.java:252)
    at junit.framework.TestSuite.run(TestSuite.java:247)
    at org.junit.internal.runners.JUnit38ClassRunner.run(JUnit38ClassRunner.java:86)
    at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:50)
    at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:459)
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:675)
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:382)
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:192)
Caused by: java.util.NoSuchElementException
    at java.util.Collections$EmptyIterator.next(Collections.java:4189)
    at org.codehaus.plexus.DefaultPlexusContainer.lookup(DefaultPlexusContainer.java:263)
    ... 24 more
