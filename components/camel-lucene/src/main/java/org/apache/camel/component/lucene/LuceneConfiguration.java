begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lucene
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
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|analysis
operator|.
name|standard
operator|.
name|StandardAnalyzer
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

begin_class
DECL|class|LuceneConfiguration
specifier|public
class|class
name|LuceneConfiguration
block|{
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
DECL|field|protocolType
specifier|private
name|String
name|protocolType
decl_stmt|;
DECL|field|authority
specifier|private
name|String
name|authority
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
DECL|field|sourceDirectory
specifier|private
name|File
name|sourceDirectory
decl_stmt|;
DECL|field|indexDirectory
specifier|private
name|File
name|indexDirectory
decl_stmt|;
DECL|field|analyzer
specifier|private
name|Analyzer
name|analyzer
decl_stmt|;
DECL|field|maxHits
specifier|private
name|int
name|maxHits
decl_stmt|;
DECL|field|luceneVersion
specifier|private
name|Version
name|luceneVersion
init|=
name|Version
operator|.
name|LUCENE_35
decl_stmt|;
DECL|method|LuceneConfiguration ()
specifier|public
name|LuceneConfiguration
parameter_list|()
block|{     }
DECL|method|LuceneConfiguration (URI uri)
specifier|public
name|LuceneConfiguration
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|parseURI (URI uri, Map<String, Object> parameters, LuceneComponent component)
specifier|public
name|void
name|parseURI
parameter_list|(
name|URI
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|LuceneComponent
name|component
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|protocol
init|=
name|uri
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"lucene"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized Lucene protocol: "
operator|+
name|protocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|setAuthority
argument_list|(
name|uri
operator|.
name|getAuthority
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isValidAuthority
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|URISyntaxException
argument_list|(
name|uri
operator|.
name|toASCIIString
argument_list|()
argument_list|,
literal|"Incorrect URI syntax and/or Operation specified for the Lucene endpoint."
operator|+
literal|"Please specify the syntax as \"lucene:[Endpoint Name]:[Operation]?[Query]\""
argument_list|)
throw|;
block|}
name|setHost
argument_list|(
name|retrieveTokenFromAuthority
argument_list|(
literal|"hostname"
argument_list|)
argument_list|)
expr_stmt|;
name|setOperation
argument_list|(
name|retrieveTokenFromAuthority
argument_list|(
literal|"operation"
argument_list|)
argument_list|)
expr_stmt|;
name|sourceDirectory
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"srcDir"
argument_list|,
name|File
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|indexDirectory
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"indexDir"
argument_list|,
name|File
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
literal|"file:///./indexDirectory"
argument_list|)
argument_list|)
expr_stmt|;
name|analyzer
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"analyzer"
argument_list|,
name|Analyzer
operator|.
name|class
argument_list|,
operator|new
name|StandardAnalyzer
argument_list|(
name|luceneVersion
argument_list|)
argument_list|)
expr_stmt|;
name|setMaxHits
argument_list|(
name|component
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"maxHits"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|isValidAuthority ()
specifier|private
name|boolean
name|isValidAuthority
parameter_list|()
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
operator|(
operator|!
name|authority
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
operator|)
operator|||
operator|(
operator|(
name|authority
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|0
index|]
operator|)
operator|==
literal|null
operator|)
operator|||
operator|(
operator|(
operator|!
name|authority
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|1
index|]
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"insert"
argument_list|)
operator|)
operator|&&
operator|(
operator|!
name|authority
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|1
index|]
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"query"
argument_list|)
operator|)
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|retrieveTokenFromAuthority (String token)
specifier|private
name|String
name|retrieveTokenFromAuthority
parameter_list|(
name|String
name|token
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|retval
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"hostname"
argument_list|)
condition|)
block|{
name|retval
operator|=
name|uri
operator|.
name|getAuthority
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|0
index|]
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|uri
operator|.
name|getAuthority
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|1
index|]
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
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
DECL|method|getProtocolType ()
specifier|public
name|String
name|getProtocolType
parameter_list|()
block|{
return|return
name|protocolType
return|;
block|}
DECL|method|setProtocolType (String protocolType)
specifier|public
name|void
name|setProtocolType
parameter_list|(
name|String
name|protocolType
parameter_list|)
block|{
name|this
operator|.
name|protocolType
operator|=
name|protocolType
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
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
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
name|int
name|getMaxHits
parameter_list|()
block|{
return|return
name|maxHits
return|;
block|}
DECL|method|setMaxHits (int maxHits)
specifier|public
name|void
name|setMaxHits
parameter_list|(
name|int
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
block|}
end_class

end_unit

