begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|builder
operator|.
name|xml
operator|.
name|Namespaces
import|;
end_import

begin_class
DECL|class|XPathWithNamespaceBuilderFilterAndResultTypeTest
specifier|public
class|class
name|XPathWithNamespaceBuilderFilterAndResultTypeTest
extends|extends
name|XPathWithNamespaceBuilderFilterTest
block|{
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
comment|// START SNIPPET: example
comment|// lets define the namespaces we'll need in our filters
name|Namespaces
name|ns
init|=
operator|new
name|Namespaces
argument_list|(
literal|"c"
argument_list|,
literal|"http://acme.com/cheese"
argument_list|)
operator|.
name|add
argument_list|(
literal|"xsd"
argument_list|,
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|)
decl_stmt|;
comment|// now lets create an xpath based Message Filter
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|filter
argument_list|(
name|ns
operator|.
name|xpath
argument_list|(
literal|"/c:person[@name='James']"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

