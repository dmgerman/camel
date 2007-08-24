begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.view
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|view
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
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
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
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|DotViewTest
specifier|public
class|class
name|DotViewTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|generator
specifier|protected
name|RouteDotGenerator
name|generator
init|=
operator|new
name|RouteDotGenerator
argument_list|()
decl_stmt|;
DECL|method|testDotFile ()
specifier|public
name|void
name|testDotFile
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|File
argument_list|(
literal|"target"
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|generator
operator|.
name|setFile
argument_list|(
literal|"target/Example.dot"
argument_list|)
expr_stmt|;
name|generator
operator|.
name|drawRoutes
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"file:foo/xyz?noop=true"
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
name|from
argument_list|(
literal|"file:foo/bar?noop=true"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:xyz?noop=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"file:xyz?noop=true"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|splitter
argument_list|(
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"/invoice/lineItems"
argument_list|)
argument_list|)
operator|.
name|throttler
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

