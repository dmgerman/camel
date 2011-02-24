begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|config
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|XmlElement
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

begin_comment
comment|/**  * Represents the XML type for&lt;properties&gt;.  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"properties"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|PropertiesDefinition
specifier|public
class|class
name|PropertiesDefinition
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"property"
argument_list|)
DECL|field|properties
specifier|private
name|List
argument_list|<
name|PropertyDefinition
argument_list|>
name|properties
decl_stmt|;
DECL|method|PropertiesDefinition ()
specifier|public
name|PropertiesDefinition
parameter_list|()
block|{     }
DECL|method|setProperties (List<PropertyDefinition> properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|List
argument_list|<
name|PropertyDefinition
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|List
argument_list|<
name|PropertyDefinition
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/***      * @return A Map of the contained DataFormatType's indexed by id.      */
DECL|method|asMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|asMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|propertiesAsMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PropertyDefinition
name|propertyType
range|:
name|getProperties
argument_list|()
control|)
block|{
name|propertiesAsMap
operator|.
name|put
argument_list|(
name|propertyType
operator|.
name|getKey
argument_list|()
argument_list|,
name|propertyType
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|propertiesAsMap
return|;
block|}
block|}
end_class

end_unit

