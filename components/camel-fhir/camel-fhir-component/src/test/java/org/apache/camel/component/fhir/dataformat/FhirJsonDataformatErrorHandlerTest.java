begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fhir.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|fhir
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|context
operator|.
name|FhirContext
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|parser
operator|.
name|DataFormatException
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|parser
operator|.
name|IParserErrorHandler
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|parser
operator|.
name|LenientErrorHandler
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|parser
operator|.
name|StrictErrorHandler
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
name|CamelExecutionException
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
name|component
operator|.
name|fhir
operator|.
name|FhirJsonDataFormat
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
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hl7
operator|.
name|fhir
operator|.
name|dstu3
operator|.
name|model
operator|.
name|Patient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|FhirJsonDataformatErrorHandlerTest
specifier|public
class|class
name|FhirJsonDataformatErrorHandlerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|INPUT
specifier|private
specifier|static
specifier|final
name|String
name|INPUT
init|=
literal|"{\"resourceType\":\"Patient\",\"extension\":[ {\"valueDateTime\":\"2011-01-02T11:13:15\"} ]}"
decl_stmt|;
DECL|field|mockEndpoint
specifier|private
name|MockEndpoint
name|mockEndpoint
decl_stmt|;
DECL|field|fhirContext
specifier|private
specifier|final
name|FhirContext
name|fhirContext
init|=
name|FhirContext
operator|.
name|forDstu3
argument_list|()
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|mockEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|DataFormatException
operator|.
name|class
argument_list|)
DECL|method|unmarshalParserErrorHandler ()
specifier|public
name|void
name|unmarshalParserErrorHandler
parameter_list|()
throws|throws
name|Throwable
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshalErrorHandlerStrict"
argument_list|,
name|INPUT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
DECL|method|unmarshalLenientErrorHandler ()
specifier|public
name|void
name|unmarshalLenientErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshalErrorHandlerLenient"
argument_list|,
name|INPUT
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Patient
name|patient
init|=
operator|(
name|Patient
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|patient
operator|.
name|getExtension
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|patient
operator|.
name|getExtension
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2011-01-02T11:13:15"
argument_list|,
name|patient
operator|.
name|getExtension
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValueAsPrimitive
argument_list|()
operator|.
name|getValueAsString
argument_list|()
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
name|FhirJsonDataFormat
name|strickErrorHandlerDataformat
init|=
name|getStrictErrorHandlerDataFormat
argument_list|()
decl_stmt|;
name|FhirJsonDataFormat
name|lenientErrorHandlerDataFormat
init|=
name|getLenientErrorHandlerDataFormat
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalErrorHandlerStrict"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|strickErrorHandlerDataformat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:errorIsThrown"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalErrorHandlerLenient"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|lenientErrorHandlerDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|FhirJsonDataFormat
name|getStrictErrorHandlerDataFormat
parameter_list|()
block|{
name|FhirJsonDataFormat
name|fhirJsonDataFormat
init|=
operator|new
name|FhirJsonDataFormat
argument_list|()
decl_stmt|;
name|fhirJsonDataFormat
operator|.
name|setFhirContext
argument_list|(
name|fhirContext
argument_list|)
expr_stmt|;
name|IParserErrorHandler
name|parserErrorHandler
init|=
operator|new
name|StrictErrorHandler
argument_list|()
decl_stmt|;
name|fhirJsonDataFormat
operator|.
name|setParserErrorHandler
argument_list|(
name|parserErrorHandler
argument_list|)
expr_stmt|;
return|return
name|fhirJsonDataFormat
return|;
block|}
specifier|private
name|FhirJsonDataFormat
name|getLenientErrorHandlerDataFormat
parameter_list|()
block|{
name|FhirJsonDataFormat
name|fhirJsonDataFormat
init|=
operator|new
name|FhirJsonDataFormat
argument_list|()
decl_stmt|;
name|fhirJsonDataFormat
operator|.
name|setFhirContext
argument_list|(
name|fhirContext
argument_list|)
expr_stmt|;
name|IParserErrorHandler
name|parserErrorHandler
init|=
operator|new
name|LenientErrorHandler
argument_list|()
decl_stmt|;
name|fhirJsonDataFormat
operator|.
name|setParserErrorHandler
argument_list|(
name|parserErrorHandler
argument_list|)
expr_stmt|;
return|return
name|fhirJsonDataFormat
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

