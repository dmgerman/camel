begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|CamelContext
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

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
name|Ignore
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

begin_comment
comment|/**  * Test case for {@link Jt400DataQueueConsumer}.  *<p>  * So that timeout semantics can be tested, an URI to an empty data queue on an  * AS400 system should be provided (in a resource named  *<code>"jt400test.properties"</code>, in a property with key  *<code>"org.apache.camel.component.jt400.emptydtaq.uri"</code>).  *</p>  */
end_comment

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Test manual"
argument_list|)
DECL|class|Jt400DataQueueConsumerTest
specifier|public
class|class
name|Jt400DataQueueConsumerTest
extends|extends
name|Assert
block|{
comment|/**      * The deviation of the actual timeout value that we permit in our timeout      * tests.      */
DECL|field|TIMEOUT_TOLERANCE
specifier|private
specifier|static
specifier|final
name|long
name|TIMEOUT_TOLERANCE
init|=
literal|300L
decl_stmt|;
comment|/**      * Timeout value in milliseconds used to test<code>receive(long)</code>.      */
DECL|field|TIMEOUT_VALUE
specifier|private
specifier|static
specifier|final
name|long
name|TIMEOUT_VALUE
init|=
literal|3999L
decl_stmt|;
comment|/**      * The amount of time in milliseconds to pass so that a call is assumed to      * be a blocking call.      */
DECL|field|BLOCKING_THRESHOLD
specifier|private
specifier|static
specifier|final
name|long
name|BLOCKING_THRESHOLD
init|=
literal|5000L
decl_stmt|;
comment|/**      * The consumer instance used in the tests.      */
DECL|field|consumer
specifier|private
name|Jt400DataQueueConsumer
name|consumer
decl_stmt|;
comment|/**      * Flag that indicates whether<code>receive()</code> has returned from      * call.      */
DECL|field|receiveFlag
specifier|private
name|boolean
name|receiveFlag
decl_stmt|;
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
comment|// Load endpoint URI
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"jt400test.properties"
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|String
name|endpointURI
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|endpointURI
operator|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"org.apache.camel.component.jt400.emptydtaq.uri"
argument_list|)
expr_stmt|;
comment|// Instantiate consumer
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Jt400Component
name|component
init|=
operator|new
name|Jt400Component
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|camel
argument_list|)
expr_stmt|;
name|consumer
operator|=
operator|(
name|Jt400DataQueueConsumer
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
name|endpointURI
argument_list|)
operator|.
name|createPollingConsumer
argument_list|()
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests whether<code>receive(long)</code> honours the<code>timeout</code> parameter.      */
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
name|TIMEOUT_VALUE
operator|+
name|TIMEOUT_TOLERANCE
argument_list|)
DECL|method|testReceiveLong ()
specifier|public
name|void
name|testReceiveLong
parameter_list|()
block|{
name|consumer
operator|.
name|receive
argument_list|(
name|TIMEOUT_VALUE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests whether receive() blocks indefinitely.      */
annotation|@
name|Test
DECL|method|testReceive ()
specifier|public
name|void
name|testReceive
parameter_list|()
throws|throws
name|InterruptedException
block|{
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|consumer
operator|.
name|receive
argument_list|()
expr_stmt|;
name|receiveFlag
operator|=
literal|true
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
specifier|final
name|long
name|startTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|receiveFlag
condition|)
block|{
if|if
condition|(
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|startTime
operator|)
operator|>
name|BLOCKING_THRESHOLD
condition|)
block|{
comment|/* Passed test. */
return|return;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|50L
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Method receive() has returned from call."
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

