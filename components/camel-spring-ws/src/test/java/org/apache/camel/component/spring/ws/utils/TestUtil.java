begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|utils
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
name|util
operator|.
name|Iterator
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
name|Source
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
name|TransformerException
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
name|springframework
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|client
operator|.
name|core
operator|.
name|SourceExtractor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|SoapHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|SoapHeaderElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|SoapMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|core
operator|.
name|MessageAddressingProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|version
operator|.
name|Addressing10
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|version
operator|.
name|Addressing200408
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|version
operator|.
name|AddressingVersion
import|;
end_import

begin_class
DECL|class|TestUtil
specifier|public
specifier|final
class|class
name|TestUtil
block|{
DECL|field|NOOP_SOURCE_EXTRACTOR
specifier|public
specifier|static
specifier|final
name|SourceExtractor
argument_list|<
name|Object
argument_list|>
name|NOOP_SOURCE_EXTRACTOR
init|=
operator|new
name|SourceExtractor
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|extractData
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|IOException
throws|,
name|TransformerException
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
DECL|method|TestUtil ()
specifier|private
name|TestUtil
parameter_list|()
block|{     }
comment|/**      * Compare the to string ignoring new lines symbol. Handy if you need to      * compare some text coming from 2 different OS.      */
DECL|method|assertEqualsIgnoreNewLinesSymbol (String expected, String actual)
specifier|public
specifier|static
name|void
name|assertEqualsIgnoreNewLinesSymbol
parameter_list|(
name|String
name|expected
parameter_list|,
name|String
name|actual
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|StringUtils
operator|.
name|deleteAny
argument_list|(
name|expected
argument_list|,
literal|"\n\r"
argument_list|)
argument_list|,
name|StringUtils
operator|.
name|deleteAny
argument_list|(
name|actual
argument_list|,
literal|"\n\r"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retrieve a WS-Addressing properties from the soapMessage      *       * @param messageContext      * @return      */
DECL|method|getWSAProperties (SoapMessage soapMessage)
specifier|public
specifier|static
name|MessageAddressingProperties
name|getWSAProperties
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|)
block|{
name|AddressingVersion
index|[]
name|versions
init|=
operator|new
name|AddressingVersion
index|[]
block|{
operator|new
name|Addressing200408
argument_list|()
block|,
operator|new
name|Addressing10
argument_list|()
block|}
decl_stmt|;
for|for
control|(
name|AddressingVersion
name|version
range|:
name|versions
control|)
block|{
if|if
condition|(
name|supports
argument_list|(
name|version
argument_list|,
name|soapMessage
argument_list|)
condition|)
block|{
name|MessageAddressingProperties
name|requestMap
init|=
name|version
operator|.
name|getMessageAddressingProperties
argument_list|(
name|soapMessage
argument_list|)
decl_stmt|;
return|return
name|requestMap
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|supports (AddressingVersion version, SoapMessage request)
specifier|private
specifier|static
name|boolean
name|supports
parameter_list|(
name|AddressingVersion
name|version
parameter_list|,
name|SoapMessage
name|request
parameter_list|)
block|{
name|SoapHeader
name|header
init|=
name|request
operator|.
name|getSoapHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|SoapHeaderElement
argument_list|>
name|iterator
init|=
name|header
operator|.
name|examineAllHeaderElements
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|SoapHeaderElement
name|headerElement
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|.
name|understands
argument_list|(
name|headerElement
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

