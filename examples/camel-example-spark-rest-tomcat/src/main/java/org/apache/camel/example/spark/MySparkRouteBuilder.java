begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.spark
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|spark
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
operator|.
name|SparkRouteBuilder
import|;
end_import

begin_comment
comment|/**  * Define REST services using {@link SparkRouteBuilder}.  */
end_comment

begin_class
DECL|class|MySparkRouteBuilder
specifier|public
class|class
name|MySparkRouteBuilder
extends|extends
name|SparkRouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this is a very simple Camel route, but we can do more routing
comment|// as SparkRouteBuilder extends the regular RouteBuilder from camel-core
comment|// which means we can do any kind of Camel Java DSL routing here
comment|// we only have a GET service, but we have PUT, POST, and all the other REST verbs we can use
name|get
argument_list|(
literal|"hello/:me"
argument_list|,
literal|"text/plain"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"Hello ${header.me}"
argument_list|)
expr_stmt|;
name|get
argument_list|(
literal|"hello/:me"
argument_list|,
literal|"application/json"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"{ \"message\": \"Hello ${header.me}\" }"
argument_list|)
expr_stmt|;
name|get
argument_list|(
literal|"hello/:me"
argument_list|,
literal|"text/xml"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"<message>Hello ${header.me}</message>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

