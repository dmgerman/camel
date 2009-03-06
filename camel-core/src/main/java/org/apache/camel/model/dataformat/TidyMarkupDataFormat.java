begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|Node
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
name|DataFormat
import|;
end_import

begin_comment
comment|/**  * Represents a wellformed HTML document (XML well Formed) {@link DataFormat}  *  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"tidyMarkup"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|TidyMarkupDataFormat
specifier|public
class|class
name|TidyMarkupDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|dataObjectType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|dataObjectType
decl_stmt|;
DECL|method|TidyMarkupDataFormat ()
specifier|public
name|TidyMarkupDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"org.apache.camel.dataformat.tagsoup.TidyMarkupDataFormat"
argument_list|)
expr_stmt|;
name|this
operator|.
name|setDataObjectType
argument_list|(
name|Node
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|TidyMarkupDataFormat (Class<?> dataObjectType)
specifier|public
name|TidyMarkupDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|dataObjectType
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|dataObjectType
operator|.
name|isAssignableFrom
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|dataObjectType
operator|.
name|isAssignableFrom
argument_list|(
name|Node
operator|.
name|class
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"TidyMarkupDataFormat only supports returning a String or a org.w3c.dom.Node object"
argument_list|)
throw|;
block|}
name|this
operator|.
name|setDataObjectType
argument_list|(
name|dataObjectType
argument_list|)
expr_stmt|;
block|}
DECL|method|setDataObjectType (Class<?> dataObjectType)
specifier|public
name|void
name|setDataObjectType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|dataObjectType
parameter_list|)
block|{
name|this
operator|.
name|dataObjectType
operator|=
name|dataObjectType
expr_stmt|;
block|}
DECL|method|getDataObjectType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getDataObjectType
parameter_list|()
block|{
return|return
name|dataObjectType
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|getDataObjectType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"dataObjectType"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

