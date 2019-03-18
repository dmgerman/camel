begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|spi
operator|.
name|HeaderFilterStrategy
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
DECL|class|CxfRsHeaderFilterStrategyTest
specifier|public
class|class
name|CxfRsHeaderFilterStrategyTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testFilterContentType ()
specifier|public
name|void
name|testFilterContentType
parameter_list|()
throws|throws
name|Exception
block|{
name|HeaderFilterStrategy
name|filter
init|=
operator|new
name|CxfRsHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong filtered result"
argument_list|,
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"content-type"
argument_list|,
literal|"just a test"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong filtered result"
argument_list|,
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"just a test"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFilterCamelHeaders ()
specifier|public
name|void
name|testFilterCamelHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|HeaderFilterStrategy
name|filter
init|=
operator|new
name|CxfRsHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong filtered result"
argument_list|,
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"just a test"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong filtered result"
argument_list|,
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_CLASS
argument_list|,
literal|"just a test"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong filtered result"
argument_list|,
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"org.apache.camel.such.Header"
argument_list|,
literal|"just a test"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Get a wrong filtered result"
argument_list|,
name|filter
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"camel.result"
argument_list|,
literal|"just a test"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

