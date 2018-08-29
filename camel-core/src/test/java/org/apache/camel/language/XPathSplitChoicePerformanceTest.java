begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|NotifyBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StopWatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|TimeUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Test manually"
argument_list|)
DECL|class|XPathSplitChoicePerformanceTest
specifier|public
class|class
name|XPathSplitChoicePerformanceTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|20
operator|*
literal|1000
decl_stmt|;
DECL|field|tiny
specifier|private
specifier|final
name|AtomicInteger
name|tiny
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|small
specifier|private
specifier|final
name|AtomicInteger
name|small
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|med
specifier|private
specifier|final
name|AtomicInteger
name|med
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|large
specifier|private
specifier|final
name|AtomicInteger
name|large
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|watch
specifier|private
specifier|final
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|createDataFile
argument_list|(
name|log
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDummy ()
specifier|public
name|void
name|testDummy
parameter_list|()
block|{
comment|// this is a manual test
block|}
DECL|method|xxTestXPatPerformanceRoute ()
specifier|public
name|void
name|xxTestXPatPerformanceRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
name|size
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|boolean
name|matches
init|=
name|notify
operator|.
name|matches
argument_list|(
literal|60
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Processed file with {} elements in: {}"
argument_list|,
name|size
argument_list|,
name|TimeUtils
operator|.
name|printDuration
argument_list|(
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Processed "
operator|+
name|tiny
operator|.
name|get
argument_list|()
operator|+
literal|" tiny messages"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Processed "
operator|+
name|small
operator|.
name|get
argument_list|()
operator|+
literal|" small messages"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Processed "
operator|+
name|med
operator|.
name|get
argument_list|()
operator|+
literal|" medium messages"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Processed "
operator|+
name|large
operator|.
name|get
argument_list|()
operator|+
literal|" large messages"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|size
operator|/
literal|10
operator|)
operator|*
literal|4
argument_list|,
name|tiny
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|size
operator|/
literal|10
operator|)
operator|*
literal|2
argument_list|,
name|small
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|size
operator|/
literal|10
operator|)
operator|*
literal|3
argument_list|,
name|med
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|size
operator|/
literal|10
operator|)
operator|*
literal|1
argument_list|,
name|large
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should complete route"
argument_list|,
name|matches
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file:target/data?initialDelay=0&delay=10&noop=true"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Starting to process file"
argument_list|)
expr_stmt|;
name|watch
operator|.
name|restart
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/orders/order"
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/order/amount< 10"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|xml
argument_list|,
name|xml
operator|.
name|contains
argument_list|(
literal|"<amount>3</amount>"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|num
init|=
name|tiny
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
if|if
condition|(
name|num
operator|%
literal|100
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Processed "
operator|+
name|num
operator|+
literal|" tiny messages"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/order/amount< 50"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|xml
argument_list|,
name|xml
operator|.
name|contains
argument_list|(
literal|"<amount>44</amount>"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|num
init|=
name|small
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
if|if
condition|(
name|num
operator|%
literal|100
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Processed "
operator|+
name|num
operator|+
literal|" small messages"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/order/amount< 100"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|xml
argument_list|,
name|xml
operator|.
name|contains
argument_list|(
literal|"<amount>88</amount>"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|num
init|=
name|med
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
if|if
condition|(
name|num
operator|%
literal|100
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Processed "
operator|+
name|num
operator|+
literal|" medium messages"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|xml
argument_list|,
name|xml
operator|.
name|contains
argument_list|(
literal|"<amount>123</amount>"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|num
init|=
name|large
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
if|if
condition|(
name|num
operator|%
literal|100
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Processed "
operator|+
name|num
operator|+
literal|" large messages"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|end
argument_list|()
comment|// choice
operator|.
name|end
argument_list|()
expr_stmt|;
comment|// split
block|}
block|}
return|;
block|}
DECL|method|createDataFile (Logger log, int size)
specifier|public
specifier|static
name|void
name|createDataFile
parameter_list|(
name|Logger
name|log
parameter_list|,
name|int
name|size
parameter_list|)
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/data"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/data"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Creating data file ..."
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/data/data.xml"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"<orders>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|fos
operator|.
name|write
argument_list|(
literal|"<order>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
operator|(
literal|"<id>"
operator|+
name|i
operator|+
literal|"</id>\n"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|num
init|=
name|i
operator|%
literal|10
decl_stmt|;
if|if
condition|(
name|num
operator|>=
literal|0
operator|&&
name|num
operator|<=
literal|3
condition|)
block|{
name|fos
operator|.
name|write
argument_list|(
literal|"<amount>3</amount>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"<customerId>333</customerId>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|num
operator|>=
literal|4
operator|&&
name|num
operator|<=
literal|5
condition|)
block|{
name|fos
operator|.
name|write
argument_list|(
literal|"<amount>44</amount>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"<customerId>444</customerId>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|num
operator|>=
literal|6
operator|&&
name|num
operator|<=
literal|8
condition|)
block|{
name|fos
operator|.
name|write
argument_list|(
literal|"<amount>88</amount>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"<customerId>888</customerId>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fos
operator|.
name|write
argument_list|(
literal|"<amount>123</amount>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"<customerId>123123</customerId>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fos
operator|.
name|write
argument_list|(
literal|"<description>bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla</description>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"</order>\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fos
operator|.
name|write
argument_list|(
literal|"</orders>"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Creating data file done."
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

