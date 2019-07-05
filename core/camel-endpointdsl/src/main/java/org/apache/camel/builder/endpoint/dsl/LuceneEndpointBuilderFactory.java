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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
comment|/**  * To insert or query from Apache Lucene databases.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|LuceneEndpointBuilderFactory
specifier|public
interface|interface
name|LuceneEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Lucene component.      */
DECL|interface|LuceneEndpointBuilder
specifier|public
interface|interface
name|LuceneEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedLuceneEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedLuceneEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * An Analyzer builds TokenStreams, which analyze text. It thus          * represents a policy for extracting index terms from text. The value          * for analyzer can be any class that extends the abstract class          * org.apache.lucene.analysis.Analyzer. Lucene also offers a rich set of          * analyzers out of the box.          *           * The option is a:<code>org.apache.lucene.analysis.Analyzer</code>          * type.          *           * Group: producer          */
DECL|method|analyzer (Object analyzer)
specifier|default
name|LuceneEndpointBuilder
name|analyzer
parameter_list|(
name|Object
name|analyzer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"analyzer"
argument_list|,
name|analyzer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An Analyzer builds TokenStreams, which analyze text. It thus          * represents a policy for extracting index terms from text. The value          * for analyzer can be any class that extends the abstract class          * org.apache.lucene.analysis.Analyzer. Lucene also offers a rich set of          * analyzers out of the box.          *           * The option will be converted to a          *<code>org.apache.lucene.analysis.Analyzer</code> type.          *           * Group: producer          */
DECL|method|analyzer (String analyzer)
specifier|default
name|LuceneEndpointBuilder
name|analyzer
parameter_list|(
name|String
name|analyzer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"analyzer"
argument_list|,
name|analyzer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A file system directory in which index files are created upon          * analysis of the document by the specified analyzer.          *           * The option is a:<code>java.io.File</code> type.          *           * Group: producer          */
DECL|method|indexDir (File indexDir)
specifier|default
name|LuceneEndpointBuilder
name|indexDir
parameter_list|(
name|File
name|indexDir
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"indexDir"
argument_list|,
name|indexDir
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A file system directory in which index files are created upon          * analysis of the document by the specified analyzer.          *           * The option will be converted to a<code>java.io.File</code> type.          *           * Group: producer          */
DECL|method|indexDir (String indexDir)
specifier|default
name|LuceneEndpointBuilder
name|indexDir
parameter_list|(
name|String
name|indexDir
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"indexDir"
argument_list|,
name|indexDir
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An integer value that limits the result set of the search operation.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|maxHits (int maxHits)
specifier|default
name|LuceneEndpointBuilder
name|maxHits
parameter_list|(
name|int
name|maxHits
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxHits"
argument_list|,
name|maxHits
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An integer value that limits the result set of the search operation.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|maxHits (String maxHits)
specifier|default
name|LuceneEndpointBuilder
name|maxHits
parameter_list|(
name|String
name|maxHits
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxHits"
argument_list|,
name|maxHits
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An optional directory containing files to be used to be analyzed and          * added to the index at producer startup.          *           * The option is a:<code>java.io.File</code> type.          *           * Group: producer          */
DECL|method|srcDir (File srcDir)
specifier|default
name|LuceneEndpointBuilder
name|srcDir
parameter_list|(
name|File
name|srcDir
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"srcDir"
argument_list|,
name|srcDir
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An optional directory containing files to be used to be analyzed and          * added to the index at producer startup.          *           * The option will be converted to a<code>java.io.File</code> type.          *           * Group: producer          */
DECL|method|srcDir (String srcDir)
specifier|default
name|LuceneEndpointBuilder
name|srcDir
parameter_list|(
name|String
name|srcDir
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"srcDir"
argument_list|,
name|srcDir
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Lucene component.      */
DECL|interface|AdvancedLuceneEndpointBuilder
specifier|public
interface|interface
name|AdvancedLuceneEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|LuceneEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|LuceneEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedLuceneEndpointBuilder
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
name|AdvancedLuceneEndpointBuilder
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
name|AdvancedLuceneEndpointBuilder
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
name|AdvancedLuceneEndpointBuilder
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
comment|/**      * Lucene (camel-lucene)      * To insert or query from Apache Lucene databases.      *       * Category: database,search      * Available as of version: 2.2      * Maven coordinates: org.apache.camel:camel-lucene      *       * Syntax:<code>lucene:host:operation</code>      *       * Path parameter: host (required)      * The URL to the lucene server      *       * Path parameter: operation (required)      * Operation to do such as insert or query.      * The value can be one of: insert,query      */
DECL|method|lucene (String path)
specifier|default
name|LuceneEndpointBuilder
name|lucene
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|LuceneEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|LuceneEndpointBuilder
implements|,
name|AdvancedLuceneEndpointBuilder
block|{
specifier|public
name|LuceneEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"lucene"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|LuceneEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

