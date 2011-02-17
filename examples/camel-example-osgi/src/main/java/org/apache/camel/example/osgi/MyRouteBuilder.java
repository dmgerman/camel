begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|osgi
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
name|spring
operator|.
name|Main
import|;
end_import

begin_comment
comment|/**  * A simple example router to show how to define the route with Java DSL  *  * @version   */
end_comment

begin_class
DECL|class|MyRouteBuilder
specifier|public
class|class
name|MyRouteBuilder
extends|extends
name|RouteBuilder
block|{
comment|/**      * Allow this route to be run as an application      */
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
operator|new
name|Main
argument_list|()
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// set up the transform bean
name|MyTransform
name|transform
init|=
operator|new
name|MyTransform
argument_list|()
decl_stmt|;
name|transform
operator|.
name|setPrefix
argument_list|(
literal|"JavaDSL"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer://myTimer?fixedRate=true&period=2000"
argument_list|)
operator|.
name|bean
argument_list|(
name|transform
argument_list|,
literal|"transform"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:ExampleRouter"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

