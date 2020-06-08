::
:: Copyright 2014-2020 Real Logic Limited.
::
:: Licensed under the Apache License, Version 2.0 (the "License");
:: you may not use this file except in compliance with the License.
:: You may obtain a copy of the License at
::
:: https://www.apache.org/licenses/LICENSE-2.0
::
:: Unless required by applicable law or agreed to in writing, software
:: distributed under the License is distributed on an "AS IS" BASIS,
:: WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
:: See the License for the specific language governing permissions and
:: limitations under the License.
::

@echo off
set /p VERSION=<..\..\..\version.txt

"%JAVA_HOME%\bin\java" ^
    -cp ..\..\..\aeron-all\build\libs\aeron-all-%VERSION%.jar ^
    -XX:BiasedLockingStartupDelay=0 ^
    -XX:+UnlockExperimentalVMOptions ^
    -XX:+TrustFinalNonStaticFields ^
    -XX:+UseParallelOldGC ^
    -Djava.net.preferIPv4Stack=true ^
    -Dagrona.disable.bounds.checks=true ^
    -Daeron.sample.messageLength=32 ^
    -Daeron.sample.messages=100000000 ^
    -Daeron.term.buffer.sparse.file=false ^
    -Daeron.mtu.length=16k ^
    -Daeron.ipc.mtu.length=16k ^
    -Daeron.archive.control.mtu.length=4k ^
    -Daeron.socket.so_sndbuf=2m ^
    -Daeron.socket.so_rcvbuf=2m ^
    -Daeron.rcv.initial.window.length=2m ^
    -Daeron.archive.file.io.max.length=2m ^
    -Daeron.sample.channel=aeron:ipc ^
    %JVM_OPTS% io.aeron.samples.archive.EmbeddedReplayThroughput %*