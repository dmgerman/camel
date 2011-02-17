begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flatpack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flatpack
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|Parser
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
name|InvalidPayloadException
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
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DelimitedEndpoint
specifier|public
class|class
name|DelimitedEndpoint
extends|extends
name|FixedLengthEndpoint
block|{
DECL|field|delimiter
specifier|private
name|char
name|delimiter
init|=
literal|','
decl_stmt|;
DECL|field|textQualifier
specifier|private
name|char
name|textQualifier
init|=
literal|'"'
decl_stmt|;
DECL|field|ignoreFirstRecord
specifier|private
name|boolean
name|ignoreFirstRecord
init|=
literal|true
decl_stmt|;
DECL|method|DelimitedEndpoint (String uri, Resource resource)
specifier|public
name|DelimitedEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Resource
name|resource
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
DECL|method|createParser (Exchange exchange)
specifier|public
name|Parser
name|createParser
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidPayloadException
throws|,
name|IOException
block|{
name|Reader
name|bodyReader
init|=
name|ExchangeHelper
operator|.
name|getMandatoryInBody
argument_list|(
name|exchange
argument_list|,
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
name|Resource
name|resource
init|=
name|getResource
argument_list|()
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
return|return
name|getParserFactory
argument_list|()
operator|.
name|newDelimitedParser
argument_list|(
name|bodyReader
argument_list|,
name|delimiter
argument_list|,
name|textQualifier
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getParserFactory
argument_list|()
operator|.
name|newDelimitedParser
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resource
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|,
name|bodyReader
argument_list|,
name|delimiter
argument_list|,
name|textQualifier
argument_list|,
name|ignoreFirstRecord
argument_list|)
return|;
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getDelimiter ()
specifier|public
name|char
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
DECL|method|setDelimiter (char delimiter)
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|char
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
DECL|method|isIgnoreFirstRecord ()
specifier|public
name|boolean
name|isIgnoreFirstRecord
parameter_list|()
block|{
return|return
name|ignoreFirstRecord
return|;
block|}
DECL|method|setIgnoreFirstRecord (boolean ignoreFirstRecord)
specifier|public
name|void
name|setIgnoreFirstRecord
parameter_list|(
name|boolean
name|ignoreFirstRecord
parameter_list|)
block|{
name|this
operator|.
name|ignoreFirstRecord
operator|=
name|ignoreFirstRecord
expr_stmt|;
block|}
DECL|method|getTextQualifier ()
specifier|public
name|char
name|getTextQualifier
parameter_list|()
block|{
return|return
name|textQualifier
return|;
block|}
DECL|method|setTextQualifier (char textQualifier)
specifier|public
name|void
name|setTextQualifier
parameter_list|(
name|char
name|textQualifier
parameter_list|)
block|{
name|this
operator|.
name|textQualifier
operator|=
name|textQualifier
expr_stmt|;
block|}
block|}
end_class

end_unit

