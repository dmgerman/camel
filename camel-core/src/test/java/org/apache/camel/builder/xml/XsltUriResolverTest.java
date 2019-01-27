begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|component
operator|.
name|xslt
operator|.
name|XsltUriResolver
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
name|Assert
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

begin_class
DECL|class|XsltUriResolverTest
specifier|public
class|class
name|XsltUriResolverTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testResolveUri ()
specifier|public
name|void
name|testResolveUri
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|XsltUriResolver
name|xsltUriResolver
init|=
operator|new
name|XsltUriResolver
argument_list|(
name|context
argument_list|,
literal|"classpath:xslt/staff/staff.xsl"
argument_list|)
decl_stmt|;
name|Source
name|source
init|=
name|xsltUriResolver
operator|.
name|resolve
argument_list|(
literal|"../../xslt/common/staff_template.xsl"
argument_list|,
literal|"classpath:xslt/staff/staff.xsl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"classpath:xslt/common/staff_template.xsl"
argument_list|,
name|source
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

