begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|$
block|{
package|package
block|}
end_package

begin_empty_stmt
empty_stmt|;
end_empty_stmt

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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
operator|.
name|xpath
import|;
end_import

begin_comment
comment|/**  * A Camel Router  */
end_comment

begin_class
DECL|class|MyRouteBuilder
specifier|public
class|class
name|MyRouteBuilder
extends|extends
name|RouteBuilder
block|{
comment|/**      * A main() so we can easily run these routing rules in our IDE      */
DECL|method|main (String... args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * Let's configure the Camel routing rules using Java code...      */
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// TODO create Camel routes here.
comment|// here is a sample which processes the input files
comment|// (leaving them in place - see the 'noop' flag)
comment|// then performs content based routing on the message
comment|// using XPath
name|from
argument_list|(
literal|"file:src/data?noop=true"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"/person/city = 'London'"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/messages/uk"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"file:target/messages/others"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

