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
name|DataFormat
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
name|Label
import|;
end_import

begin_comment
comment|/**  * Castor data format  *  * @version   */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"dataformat,transformation"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"castor"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CastorDataFormat
specifier|public
class|class
name|CastorDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|mappingFile
specifier|private
name|String
name|mappingFile
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|validation
specifier|private
name|Boolean
name|validation
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|packages
specifier|private
name|String
index|[]
name|packages
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|classes
specifier|private
name|String
index|[]
name|classes
decl_stmt|;
DECL|method|CastorDataFormat ()
specifier|public
name|CastorDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"castor"
argument_list|)
expr_stmt|;
block|}
DECL|method|isValidation ()
specifier|public
name|boolean
name|isValidation
parameter_list|()
block|{
comment|// defaults to true if not configured
return|return
name|validation
operator|!=
literal|null
condition|?
name|validation
else|:
literal|true
return|;
block|}
DECL|method|getValidation ()
specifier|public
name|Boolean
name|getValidation
parameter_list|()
block|{
return|return
name|validation
return|;
block|}
comment|/**      * Whether validation is turned on or off.      *<p/>      * Is by default true.      */
DECL|method|setValidation (Boolean validation)
specifier|public
name|void
name|setValidation
parameter_list|(
name|Boolean
name|validation
parameter_list|)
block|{
name|this
operator|.
name|validation
operator|=
name|validation
expr_stmt|;
block|}
DECL|method|getMappingFile ()
specifier|public
name|String
name|getMappingFile
parameter_list|()
block|{
return|return
name|mappingFile
return|;
block|}
comment|/**      * Path to a Castor mapping file to load from the classpath.      */
DECL|method|setMappingFile (String mappingFile)
specifier|public
name|void
name|setMappingFile
parameter_list|(
name|String
name|mappingFile
parameter_list|)
block|{
name|this
operator|.
name|mappingFile
operator|=
name|mappingFile
expr_stmt|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
index|[]
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
comment|/**      * Add additional packages to Castor XmlContext      */
DECL|method|setPackages (String[] packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
index|[]
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|getClasses ()
specifier|public
name|String
index|[]
name|getClasses
parameter_list|()
block|{
return|return
name|classes
return|;
block|}
comment|/**      * Add additional class names to Castor XmlContext      */
DECL|method|setClasses (String[] classes)
specifier|public
name|void
name|setClasses
parameter_list|(
name|String
index|[]
name|classes
parameter_list|)
block|{
name|this
operator|.
name|classes
operator|=
name|classes
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * Encoding to use when marshalling an Object to XML.      *<p/>      * Is by default UTF-8      */
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat, CamelContext camelContext)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|mappingFile
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"mappingFile"
argument_list|,
name|mappingFile
argument_list|)
expr_stmt|;
block|}
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"validation"
argument_list|,
name|isValidation
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"encoding"
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|packages
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"packages"
argument_list|,
name|packages
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|classes
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"classes"
argument_list|,
name|classes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

