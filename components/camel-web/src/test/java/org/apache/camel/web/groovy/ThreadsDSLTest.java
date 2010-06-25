begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|groovy
package|;
end_package

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
comment|/**  *  */
end_comment

begin_class
DECL|class|ThreadsDSLTest
specifier|public
class|class
name|ThreadsDSLTest
extends|extends
name|GroovyRendererTestSupport
block|{
annotation|@
name|Test
DECL|method|testThreadsAsyncDeadLetterChannel ()
specifier|public
name|void
name|testThreadsAsyncDeadLetterChannel
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"errorHandler(deadLetterChannel(\"mock://dead\").maximumRedeliveries(2).redeliverDelay(0).logStackTrace(false).handled(false));"
operator|+
literal|"from(\"direct:start\").threads(3).to(\"mock:result\")"
decl_stmt|;
name|String
name|expected
init|=
literal|"errorHandler(deadLetterChannel(\"mock://dead\").maximumRedeliveries(2).redeliverDelay(0).handled(false));"
operator|+
literal|"from(\"direct:start\").threads(3).to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadsAsyncRoute ()
specifier|public
name|void
name|testThreadsAsyncRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"from(\"direct:start\").transform(body().append(\" World\")).threads().to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

