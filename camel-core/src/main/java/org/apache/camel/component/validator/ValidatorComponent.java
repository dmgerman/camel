begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|ls
operator|.
name|LSResourceResolver
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
name|Endpoint
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
name|DefaultComponent
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
name|ProcessorEndpoint
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
name|processor
operator|.
name|validation
operator|.
name|ValidatingProcessor
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
name|util
operator|.
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/validator.html">Validator Component</a>  * for validating XML against some schema  */
end_comment

begin_class
DECL|class|ValidatorComponent
specifier|public
class|class
name|ValidatorComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ValidatorComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|resourceUri
init|=
name|remaining
decl_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|ValidatingProcessor
name|validator
init|=
operator|new
name|ValidatingProcessor
argument_list|()
decl_stmt|;
name|validator
operator|.
name|setSchemaSource
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} using schema resource: {}"
argument_list|,
name|this
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|configureValidator
argument_list|(
name|validator
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// force loading of schema at create time otherwise concurrent
comment|// processing could cause thread safe issues for the javax.xml.validation.SchemaFactory
name|validator
operator|.
name|loadSchema
argument_list|()
expr_stmt|;
return|return
operator|new
name|ProcessorEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|validator
argument_list|)
return|;
block|}
DECL|method|configureValidator (ValidatingProcessor validator, String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|void
name|configureValidator
parameter_list|(
name|ValidatingProcessor
name|validator
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|LSResourceResolver
name|resourceResolver
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"resourceResolver"
argument_list|,
name|LSResourceResolver
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceResolver
operator|!=
literal|null
condition|)
block|{
name|validator
operator|.
name|setResourceResolver
argument_list|(
name|resourceResolver
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|validator
operator|.
name|setResourceResolver
argument_list|(
operator|new
name|DefaultLSResourceResolver
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|remaining
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|validator
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

