begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|model
operator|.
name|DataFormatDefinition
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * TidyMarkup data format is used for parsing HTML and return it as pretty  * well-formed HTML.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.0.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation"
argument_list|,
name|title
operator|=
literal|"TidyMarkup"
argument_list|)
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
name|name
operator|=
literal|"dataObjectType"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"org.w3c.dom.Node"
argument_list|)
DECL|field|dataObjectTypeName
specifier|private
name|String
name|dataObjectTypeName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|omitXmlDeclaration
specifier|private
name|Boolean
name|omitXmlDeclaration
decl_stmt|;
annotation|@
name|XmlTransient
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
literal|"tidyMarkup"
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
comment|/**      * What data type to unmarshal as, can either be org.w3c.dom.Node or      * java.lang.String.      *<p/>      * Is by default org.w3c.dom.Node      */
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
DECL|method|getDataObjectTypeName ()
specifier|public
name|String
name|getDataObjectTypeName
parameter_list|()
block|{
return|return
name|dataObjectTypeName
return|;
block|}
comment|/**      * What data type to unmarshal as, can either be org.w3c.dom.Node or      * java.lang.String.      *<p/>      * Is by default org.w3c.dom.Node      */
DECL|method|setDataObjectTypeName (String dataObjectTypeName)
specifier|public
name|void
name|setDataObjectTypeName
parameter_list|(
name|String
name|dataObjectTypeName
parameter_list|)
block|{
name|this
operator|.
name|dataObjectTypeName
operator|=
name|dataObjectTypeName
expr_stmt|;
block|}
DECL|method|getOmitXmlDeclaration ()
specifier|public
name|Boolean
name|getOmitXmlDeclaration
parameter_list|()
block|{
return|return
name|omitXmlDeclaration
return|;
block|}
comment|/**      * When returning a String, do we omit the XML declaration in the top.      */
DECL|method|setOmitXmlDeclaration (Boolean omitXmlDeclaration)
specifier|public
name|void
name|setOmitXmlDeclaration
parameter_list|(
name|Boolean
name|omitXmlDeclaration
parameter_list|)
block|{
name|this
operator|.
name|omitXmlDeclaration
operator|=
name|omitXmlDeclaration
expr_stmt|;
block|}
block|}
end_class

end_unit

