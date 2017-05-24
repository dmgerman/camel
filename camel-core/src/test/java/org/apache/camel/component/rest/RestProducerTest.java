begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultMessage
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
DECL|class|RestProducerTest
specifier|public
class|class
name|RestProducerTest
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldCreateDefinedQueryParameters ()
specifier|public
name|void
name|shouldCreateDefinedQueryParameters
parameter_list|()
throws|throws
name|UnsupportedEncodingException
throws|,
name|URISyntaxException
block|{
name|assertEquals
argument_list|(
literal|"param=value"
argument_list|,
name|RestProducer
operator|.
name|createQueryParameters
argument_list|(
literal|"param=value"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCreateOptionalPlaceholderQueryParametersForPresentValues ()
specifier|public
name|void
name|shouldCreateOptionalPlaceholderQueryParametersForPresentValues
parameter_list|()
throws|throws
name|UnsupportedEncodingException
throws|,
name|URISyntaxException
block|{
specifier|final
name|DefaultMessage
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"param"
argument_list|,
literal|"header"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"param=header"
argument_list|,
name|RestProducer
operator|.
name|createQueryParameters
argument_list|(
literal|"param={param?}"
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCreatePlaceholderQueryParameters ()
specifier|public
name|void
name|shouldCreatePlaceholderQueryParameters
parameter_list|()
throws|throws
name|UnsupportedEncodingException
throws|,
name|URISyntaxException
block|{
specifier|final
name|DefaultMessage
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"param"
argument_list|,
literal|"header"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"param=header"
argument_list|,
name|RestProducer
operator|.
name|createQueryParameters
argument_list|(
literal|"param={param}"
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCreateQueryNoParameters ()
specifier|public
name|void
name|shouldCreateQueryNoParameters
parameter_list|()
throws|throws
name|UnsupportedEncodingException
throws|,
name|URISyntaxException
block|{
name|assertNull
argument_list|(
name|RestProducer
operator|.
name|createQueryParameters
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotCreateOptionalPlaceholderQueryParametersForMissingValues ()
specifier|public
name|void
name|shouldNotCreateOptionalPlaceholderQueryParametersForMissingValues
parameter_list|()
throws|throws
name|UnsupportedEncodingException
throws|,
name|URISyntaxException
block|{
specifier|final
name|DefaultMessage
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|RestProducer
operator|.
name|createQueryParameters
argument_list|(
literal|"param={param?}"
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportAllCombinations ()
specifier|public
name|void
name|shouldSupportAllCombinations
parameter_list|()
throws|throws
name|UnsupportedEncodingException
throws|,
name|URISyntaxException
block|{
specifier|final
name|DefaultMessage
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"required"
argument_list|,
literal|"header_required"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"optional_present"
argument_list|,
literal|"header_optional_present"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"given=value&required=header_required&optional_present=header_optional_present"
argument_list|,
name|RestProducer
operator|.
name|createQueryParameters
argument_list|(
literal|"given=value&required={required}&optional={optional?}&optional_present={optional_present?}"
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

