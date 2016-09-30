begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lucene.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lucene
operator|.
name|springboot
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
name|java
operator|.
name|net
operator|.
name|URI
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
name|lucene
operator|.
name|LuceneOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|Analyzer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|util
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * To insert or query from Apache Lucene databases.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.lucene"
argument_list|)
DECL|class|LuceneComponentConfiguration
specifier|public
class|class
name|LuceneComponentConfiguration
block|{
comment|/**      * To use a shared lucene configuration      */
DECL|field|config
specifier|private
name|LuceneConfigurationNestedConfiguration
name|config
decl_stmt|;
DECL|method|getConfig ()
specifier|public
name|LuceneConfigurationNestedConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setConfig (LuceneConfigurationNestedConfiguration config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|LuceneConfigurationNestedConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|class|LuceneConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|LuceneConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lucene
operator|.
name|LuceneConfiguration
operator|.
name|class
decl_stmt|;
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
comment|/**          * The URL to the lucene server          */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**          * Operation to do such as insert or query.          */
DECL|field|operation
specifier|private
name|LuceneOperation
name|operation
decl_stmt|;
DECL|field|authority
specifier|private
name|String
name|authority
decl_stmt|;
comment|/**          * An optional directory containing files to be used to be analyzed and          * added to the index at producer startup.          */
DECL|field|sourceDirectory
specifier|private
name|File
name|sourceDirectory
decl_stmt|;
comment|/**          * A file system directory in which index files are created upon          * analysis of the document by the specified analyzer          */
DECL|field|indexDirectory
specifier|private
name|File
name|indexDirectory
decl_stmt|;
comment|/**          * An Analyzer builds TokenStreams, which analyze text. It thus          * represents a policy for extracting index terms from text. The value          * for analyzer can be any class that extends the abstract class          * org.apache.lucene.analysis.Analyzer. Lucene also offers a rich set of          * analyzers out of the box          */
DECL|field|analyzer
specifier|private
name|Analyzer
name|analyzer
decl_stmt|;
comment|/**          * An integer value that limits the result set of the search operation          */
DECL|field|maxHits
specifier|private
name|Integer
name|maxHits
decl_stmt|;
annotation|@
name|NestedConfigurationProperty
DECL|field|luceneVersion
specifier|private
name|Version
name|luceneVersion
decl_stmt|;
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUri (URI uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|LuceneOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (LuceneOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|LuceneOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getAuthority ()
specifier|public
name|String
name|getAuthority
parameter_list|()
block|{
return|return
name|authority
return|;
block|}
DECL|method|setAuthority (String authority)
specifier|public
name|void
name|setAuthority
parameter_list|(
name|String
name|authority
parameter_list|)
block|{
name|this
operator|.
name|authority
operator|=
name|authority
expr_stmt|;
block|}
DECL|method|getSourceDirectory ()
specifier|public
name|File
name|getSourceDirectory
parameter_list|()
block|{
return|return
name|sourceDirectory
return|;
block|}
DECL|method|setSourceDirectory (File sourceDirectory)
specifier|public
name|void
name|setSourceDirectory
parameter_list|(
name|File
name|sourceDirectory
parameter_list|)
block|{
name|this
operator|.
name|sourceDirectory
operator|=
name|sourceDirectory
expr_stmt|;
block|}
DECL|method|getIndexDirectory ()
specifier|public
name|File
name|getIndexDirectory
parameter_list|()
block|{
return|return
name|indexDirectory
return|;
block|}
DECL|method|setIndexDirectory (File indexDirectory)
specifier|public
name|void
name|setIndexDirectory
parameter_list|(
name|File
name|indexDirectory
parameter_list|)
block|{
name|this
operator|.
name|indexDirectory
operator|=
name|indexDirectory
expr_stmt|;
block|}
DECL|method|getAnalyzer ()
specifier|public
name|Analyzer
name|getAnalyzer
parameter_list|()
block|{
return|return
name|analyzer
return|;
block|}
DECL|method|setAnalyzer (Analyzer analyzer)
specifier|public
name|void
name|setAnalyzer
parameter_list|(
name|Analyzer
name|analyzer
parameter_list|)
block|{
name|this
operator|.
name|analyzer
operator|=
name|analyzer
expr_stmt|;
block|}
DECL|method|getMaxHits ()
specifier|public
name|Integer
name|getMaxHits
parameter_list|()
block|{
return|return
name|maxHits
return|;
block|}
DECL|method|setMaxHits (Integer maxHits)
specifier|public
name|void
name|setMaxHits
parameter_list|(
name|Integer
name|maxHits
parameter_list|)
block|{
name|this
operator|.
name|maxHits
operator|=
name|maxHits
expr_stmt|;
block|}
DECL|method|getLuceneVersion ()
specifier|public
name|Version
name|getLuceneVersion
parameter_list|()
block|{
return|return
name|luceneVersion
return|;
block|}
DECL|method|setLuceneVersion (Version luceneVersion)
specifier|public
name|void
name|setLuceneVersion
parameter_list|(
name|Version
name|luceneVersion
parameter_list|)
block|{
name|this
operator|.
name|luceneVersion
operator|=
name|luceneVersion
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

