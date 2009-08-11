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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|StopDSLTest
specifier|public
class|class
name|StopDSLTest
extends|extends
name|GroovyRendererTestSupport
block|{
DECL|method|testStop ()
specifier|public
name|void
name|testStop
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|DSL
init|=
literal|"from(\"direct:start\").choice().when(body().contains(\"Hello\")).to(\"mock:hello\").when(body().contains(\"Bye\")).to(\"mock:bye\").stop()"
operator|+
literal|".otherwise().to(\"mock:other\").end().to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
name|DSL
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|DSL
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

