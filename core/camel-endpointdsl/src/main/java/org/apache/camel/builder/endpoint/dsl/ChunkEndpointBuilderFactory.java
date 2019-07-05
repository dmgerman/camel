begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|EndpointConsumerBuilder
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
name|EndpointProducerBuilder
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
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * Transforms the message using a Chunk template.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ChunkEndpointBuilderFactory
specifier|public
interface|interface
name|ChunkEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Chunk component.      */
DECL|interface|ChunkEndpointBuilder
specifier|public
interface|interface
name|ChunkEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedChunkEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedChunkEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Path to the resource. You can prefix with: classpath, file, http,          * ref, or bean. classpath, file and http loads the resource using these          * protocols (classpath is default). ref will lookup the resource in the          * registry. bean will call a method on a bean to be used as the          * resource. For bean you can specify the method name after dot, eg          * bean:myBean.myMethod.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|resourceUri (String resourceUri)
specifier|default
name|ChunkEndpointBuilder
name|resourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceUri"
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to use resource content cache or not.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|contentCache (boolean contentCache)
specifier|default
name|ChunkEndpointBuilder
name|contentCache
parameter_list|(
name|boolean
name|contentCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"contentCache"
argument_list|,
name|contentCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to use resource content cache or not.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|contentCache (String contentCache)
specifier|default
name|ChunkEndpointBuilder
name|contentCache
parameter_list|(
name|String
name|contentCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"contentCache"
argument_list|,
name|contentCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the encoding of the body.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|encoding (String encoding)
specifier|default
name|ChunkEndpointBuilder
name|encoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"encoding"
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the file extension of the template.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|extension (String extension)
specifier|default
name|ChunkEndpointBuilder
name|extension
parameter_list|(
name|String
name|extension
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"extension"
argument_list|,
name|extension
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the themes folder to scan.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|themeFolder (String themeFolder)
specifier|default
name|ChunkEndpointBuilder
name|themeFolder
parameter_list|(
name|String
name|themeFolder
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"themeFolder"
argument_list|,
name|themeFolder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the theme layer to elaborate.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|themeLayer (String themeLayer)
specifier|default
name|ChunkEndpointBuilder
name|themeLayer
parameter_list|(
name|String
name|themeLayer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"themeLayer"
argument_list|,
name|themeLayer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the themes subfolder to scan.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|themeSubfolder (String themeSubfolder)
specifier|default
name|ChunkEndpointBuilder
name|themeSubfolder
parameter_list|(
name|String
name|themeSubfolder
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"themeSubfolder"
argument_list|,
name|themeSubfolder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Chunk component.      */
DECL|interface|AdvancedChunkEndpointBuilder
specifier|public
interface|interface
name|AdvancedChunkEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ChunkEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ChunkEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedChunkEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedChunkEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedChunkEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedChunkEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Transforms the message using a Chunk template.      * Maven coordinates: org.apache.camel:camel-chunk      */
DECL|method|chunk (String path)
specifier|default
name|ChunkEndpointBuilder
name|chunk
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ChunkEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ChunkEndpointBuilder
implements|,
name|AdvancedChunkEndpointBuilder
block|{
specifier|public
name|ChunkEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"chunk"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ChunkEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

