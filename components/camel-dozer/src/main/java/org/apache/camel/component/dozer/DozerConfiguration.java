begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dozer
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|dozer
operator|.
name|DozerBeanMapperConfiguration
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
name|UriParam
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
name|UriParams
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
name|UriPath
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|dozermapper
operator|.
name|core
operator|.
name|util
operator|.
name|DozerConstants
operator|.
name|DEFAULT_MAPPING_FILE
import|;
end_import

begin_comment
comment|/**  * Configuration used for a Dozer endpoint.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|DozerConfiguration
specifier|public
class|class
name|DozerConfiguration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
DECL|field|marshalId
specifier|private
name|String
name|marshalId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|unmarshalId
specifier|private
name|String
name|unmarshalId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sourceModel
specifier|private
name|String
name|sourceModel
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|targetModel
specifier|private
name|String
name|targetModel
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|DEFAULT_MAPPING_FILE
argument_list|)
DECL|field|mappingFile
specifier|private
name|String
name|mappingFile
decl_stmt|;
annotation|@
name|UriParam
DECL|field|mappingConfiguration
specifier|private
name|DozerBeanMapperConfiguration
name|mappingConfiguration
decl_stmt|;
DECL|method|DozerConfiguration ()
specifier|public
name|DozerConfiguration
parameter_list|()
block|{
name|setMappingFile
argument_list|(
name|DEFAULT_MAPPING_FILE
argument_list|)
expr_stmt|;
block|}
DECL|method|getMarshalId ()
specifier|public
name|String
name|getMarshalId
parameter_list|()
block|{
return|return
name|marshalId
return|;
block|}
comment|/**      * The id of a dataFormat defined within the Camel Context to use for marshalling the mapping output to a non-Java type.      */
DECL|method|setMarshalId (String marshalId)
specifier|public
name|void
name|setMarshalId
parameter_list|(
name|String
name|marshalId
parameter_list|)
block|{
name|this
operator|.
name|marshalId
operator|=
name|marshalId
expr_stmt|;
block|}
DECL|method|getUnmarshalId ()
specifier|public
name|String
name|getUnmarshalId
parameter_list|()
block|{
return|return
name|unmarshalId
return|;
block|}
comment|/**      * The id of a dataFormat defined within the Camel Context to use for unmarshalling the mapping input from a non-Java type.      */
DECL|method|setUnmarshalId (String unmarshalId)
specifier|public
name|void
name|setUnmarshalId
parameter_list|(
name|String
name|unmarshalId
parameter_list|)
block|{
name|this
operator|.
name|unmarshalId
operator|=
name|unmarshalId
expr_stmt|;
block|}
DECL|method|getSourceModel ()
specifier|public
name|String
name|getSourceModel
parameter_list|()
block|{
return|return
name|sourceModel
return|;
block|}
comment|/**      * Fully-qualified class name for the source type used in the mapping. If specified, the input to the mapping is converted to the specified type before being mapped with Dozer.      */
DECL|method|setSourceModel (String sourceModel)
specifier|public
name|void
name|setSourceModel
parameter_list|(
name|String
name|sourceModel
parameter_list|)
block|{
name|this
operator|.
name|sourceModel
operator|=
name|sourceModel
expr_stmt|;
block|}
DECL|method|getTargetModel ()
specifier|public
name|String
name|getTargetModel
parameter_list|()
block|{
return|return
name|targetModel
return|;
block|}
comment|/**      * Fully-qualified class name for the target type used in the mapping.      */
DECL|method|setTargetModel (String targetModel)
specifier|public
name|void
name|setTargetModel
parameter_list|(
name|String
name|targetModel
parameter_list|)
block|{
name|this
operator|.
name|targetModel
operator|=
name|targetModel
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * A human readable name of the mapping.      */
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
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
comment|/**      * The location of a Dozer configuration file. The file is loaded from the classpath by default,      * but you can use file:, classpath:, or http: to load the configuration from a specific location.      */
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
DECL|method|getMappingConfiguration ()
specifier|public
name|DozerBeanMapperConfiguration
name|getMappingConfiguration
parameter_list|()
block|{
return|return
name|mappingConfiguration
return|;
block|}
comment|/**      * The name of a DozerBeanMapperConfiguration bean in the Camel registry which should be used for configuring the Dozer mapping.      * This is an alternative to the mappingFile option that can be used for fine-grained control over how Dozer is configured.      * Remember to use a "#" prefix in the value to indicate that the bean is in the Camel registry (e.g. "#myDozerConfig").      */
DECL|method|setMappingConfiguration (DozerBeanMapperConfiguration mappingConfiguration)
specifier|public
name|void
name|setMappingConfiguration
parameter_list|(
name|DozerBeanMapperConfiguration
name|mappingConfiguration
parameter_list|)
block|{
name|this
operator|.
name|mappingConfiguration
operator|=
name|mappingConfiguration
expr_stmt|;
block|}
block|}
end_class

end_unit

