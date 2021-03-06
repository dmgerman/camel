begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.schematron.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|schematron
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|RuntimeCamelException
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
name|schematron
operator|.
name|constant
operator|.
name|Constants
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

begin_import
import|import
name|org
operator|.
name|xmlunit
operator|.
name|builder
operator|.
name|Input
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xmlunit
operator|.
name|xpath
operator|.
name|JAXPXPathEngine
import|;
end_import

begin_comment
comment|/**  * Utility Class.  */
end_comment

begin_class
DECL|class|Utils
specifier|public
specifier|final
class|class
name|Utils
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Utils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|Utils ()
specifier|private
name|Utils
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"This is a utility class"
argument_list|)
throw|;
block|}
comment|/**      * Evaluate an XPATH expression.      */
DECL|method|evaluate (final String xpath, final String xml)
specifier|public
specifier|static
name|String
name|evaluate
parameter_list|(
specifier|final
name|String
name|xpath
parameter_list|,
specifier|final
name|String
name|xml
parameter_list|)
block|{
name|JAXPXPathEngine
name|xpathEngine
init|=
operator|new
name|JAXPXPathEngine
argument_list|()
decl_stmt|;
name|xpathEngine
operator|.
name|setNamespaceContext
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"svrl"
argument_list|,
name|Constants
operator|.
name|HTTP_PURL_OCLC_ORG_DSDL_SVRL
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|xpathEngine
operator|.
name|evaluate
argument_list|(
name|xpath
argument_list|,
name|Input
operator|.
name|fromString
argument_list|(
name|xml
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Failed to apply xpath {} on xml {}"
argument_list|,
name|xpath
argument_list|,
name|xml
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

