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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|Exchange
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
name|converter
operator|.
name|IOConverter
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
name|document
operator|.
name|Document
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
name|document
operator|.
name|Field
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
name|document
operator|.
name|FieldType
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
name|index
operator|.
name|IndexWriter
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
name|index
operator|.
name|IndexWriterConfig
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
name|index
operator|.
name|IndexWriterConfig
operator|.
name|OpenMode
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
name|store
operator|.
name|NIOFSDirectory
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|LuceneIndexer
specifier|public
class|class
name|LuceneIndexer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LuceneIndexer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|sourceDirectory
specifier|private
name|File
name|sourceDirectory
decl_stmt|;
DECL|field|fieldTypeAnalyzed
specifier|private
name|FieldType
name|fieldTypeAnalyzed
decl_stmt|;
DECL|field|fieldTypeUnanalyzed
specifier|private
name|FieldType
name|fieldTypeUnanalyzed
decl_stmt|;
DECL|field|analyzer
specifier|private
name|Analyzer
name|analyzer
decl_stmt|;
DECL|field|niofsDirectory
specifier|private
name|NIOFSDirectory
name|niofsDirectory
decl_stmt|;
DECL|field|indexWriter
specifier|private
name|IndexWriter
name|indexWriter
decl_stmt|;
DECL|field|sourceDirectoryIndexed
specifier|private
name|boolean
name|sourceDirectoryIndexed
decl_stmt|;
DECL|method|LuceneIndexer (File sourceDirectory, File indexDirectory, Analyzer analyzer)
specifier|public
name|LuceneIndexer
parameter_list|(
name|File
name|sourceDirectory
parameter_list|,
name|File
name|indexDirectory
parameter_list|,
name|Analyzer
name|analyzer
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|indexDirectory
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|indexDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
name|indexDirectory
operator|.
name|mkdir
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|setNiofsDirectory
argument_list|(
operator|new
name|NIOFSDirectory
argument_list|(
name|indexDirectory
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|setNiofsDirectory
argument_list|(
operator|new
name|NIOFSDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"./indexDirectory"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|fieldTypeAnalyzed
operator|=
operator|new
name|FieldType
argument_list|()
expr_stmt|;
name|fieldTypeAnalyzed
operator|.
name|setIndexed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fieldTypeAnalyzed
operator|.
name|setStored
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fieldTypeAnalyzed
operator|.
name|setTokenized
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fieldTypeAnalyzed
operator|.
name|freeze
argument_list|()
expr_stmt|;
name|fieldTypeUnanalyzed
operator|=
operator|new
name|FieldType
argument_list|()
expr_stmt|;
name|fieldTypeUnanalyzed
operator|.
name|setIndexed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fieldTypeUnanalyzed
operator|.
name|setStored
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fieldTypeUnanalyzed
operator|.
name|setTokenized
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|fieldTypeUnanalyzed
operator|.
name|freeze
argument_list|()
expr_stmt|;
name|this
operator|.
name|setAnalyzer
argument_list|(
name|analyzer
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|sourceDirectory
operator|!=
literal|null
operator|)
operator|&&
operator|(
operator|!
name|sourceDirectoryIndexed
operator|)
condition|)
block|{
name|this
operator|.
name|setSourceDirectory
argument_list|(
name|sourceDirectory
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|getSourceDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|sourceDirectoryIndexed
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|index (Exchange exchange)
specifier|public
name|void
name|index
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Indexing {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|openIndexWriter
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|add
argument_list|(
literal|"exchangeId"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|field
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|field
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|add
argument_list|(
literal|"contents"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|closeIndexWriter
argument_list|()
expr_stmt|;
block|}
DECL|method|getNiofsDirectory ()
specifier|public
name|NIOFSDirectory
name|getNiofsDirectory
parameter_list|()
block|{
return|return
name|niofsDirectory
return|;
block|}
DECL|method|setNiofsDirectory (NIOFSDirectory niofsDirectory)
specifier|public
name|void
name|setNiofsDirectory
parameter_list|(
name|NIOFSDirectory
name|niofsDirectory
parameter_list|)
block|{
name|this
operator|.
name|niofsDirectory
operator|=
name|niofsDirectory
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
DECL|method|add (String field, String value, boolean analyzed)
specifier|private
name|void
name|add
parameter_list|(
name|String
name|field
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|analyzed
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Adding field: {}"
argument_list|,
name|field
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"       value: {}"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|Document
name|doc
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|Field
argument_list|(
name|field
argument_list|,
name|value
argument_list|,
name|analyzed
condition|?
name|fieldTypeAnalyzed
else|:
name|fieldTypeUnanalyzed
argument_list|)
argument_list|)
expr_stmt|;
name|indexWriter
operator|.
name|addDocument
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
DECL|method|add (File file)
specifier|private
name|void
name|add
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|file
operator|.
name|canRead
argument_list|()
condition|)
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|String
index|[]
name|files
init|=
name|file
operator|.
name|list
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|child
range|:
name|files
control|)
block|{
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/"
operator|+
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Adding {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|openIndexWriter
argument_list|()
expr_stmt|;
name|add
argument_list|(
literal|"path"
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|"contents"
argument_list|,
operator|new
name|String
argument_list|(
name|IOConverter
operator|.
name|toByteArray
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|closeIndexWriter
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Added {} successfully"
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Directory/File "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" could not be read."
operator|+
literal|" This directory will not be indexed. Please check permissions and rebuild indexes."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|openIndexWriter ()
specifier|private
name|void
name|openIndexWriter
parameter_list|()
throws|throws
name|IOException
block|{
name|IndexWriterConfig
name|indexWriterConfig
decl_stmt|;
comment|// use create or append so we can reuse existing index if already exists
name|indexWriterConfig
operator|=
operator|new
name|IndexWriterConfig
argument_list|(
name|Version
operator|.
name|LUCENE_44
argument_list|,
name|getAnalyzer
argument_list|()
argument_list|)
operator|.
name|setOpenMode
argument_list|(
name|OpenMode
operator|.
name|CREATE_OR_APPEND
argument_list|)
expr_stmt|;
name|indexWriter
operator|=
operator|new
name|IndexWriter
argument_list|(
name|niofsDirectory
argument_list|,
name|indexWriterConfig
argument_list|)
expr_stmt|;
block|}
DECL|method|closeIndexWriter ()
specifier|private
name|void
name|closeIndexWriter
parameter_list|()
throws|throws
name|IOException
block|{
name|indexWriter
operator|.
name|commit
argument_list|()
expr_stmt|;
name|indexWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

