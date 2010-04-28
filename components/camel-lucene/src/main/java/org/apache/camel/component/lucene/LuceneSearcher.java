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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|lucene
operator|.
name|support
operator|.
name|Hit
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
name|processor
operator|.
name|lucene
operator|.
name|support
operator|.
name|Hits
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|queryParser
operator|.
name|ParseException
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
name|queryParser
operator|.
name|QueryParser
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
name|search
operator|.
name|IndexSearcher
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
name|search
operator|.
name|Query
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
name|search
operator|.
name|ScoreDoc
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
name|search
operator|.
name|TopScoreDocCollector
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

begin_class
DECL|class|LuceneSearcher
specifier|public
class|class
name|LuceneSearcher
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|LuceneSearcher
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|analyzer
specifier|private
name|Analyzer
name|analyzer
decl_stmt|;
DECL|field|indexSearcher
specifier|private
name|IndexSearcher
name|indexSearcher
decl_stmt|;
DECL|field|hits
specifier|private
name|ScoreDoc
index|[]
name|hits
decl_stmt|;
DECL|method|open (File indexDirectory, Analyzer analyzer)
specifier|public
name|void
name|open
parameter_list|(
name|File
name|indexDirectory
parameter_list|,
name|Analyzer
name|analyzer
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|indexDirectory
operator|!=
literal|null
condition|)
block|{
name|indexSearcher
operator|=
operator|new
name|IndexSearcher
argument_list|(
operator|new
name|NIOFSDirectory
argument_list|(
name|indexDirectory
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|indexSearcher
operator|=
operator|new
name|IndexSearcher
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
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|analyzer
operator|=
name|analyzer
expr_stmt|;
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|indexSearcher
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|search (String searchPhrase, int maxNumberOfHits)
specifier|public
name|Hits
name|search
parameter_list|(
name|String
name|searchPhrase
parameter_list|,
name|int
name|maxNumberOfHits
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|search
argument_list|(
name|searchPhrase
argument_list|,
name|maxNumberOfHits
argument_list|,
name|Version
operator|.
name|LUCENE_30
argument_list|)
return|;
block|}
DECL|method|search (String searchPhrase, int maxNumberOfHits, Version luenceVersion)
specifier|public
name|Hits
name|search
parameter_list|(
name|String
name|searchPhrase
parameter_list|,
name|int
name|maxNumberOfHits
parameter_list|,
name|Version
name|luenceVersion
parameter_list|)
throws|throws
name|Exception
block|{
name|Hits
name|searchHits
init|=
operator|new
name|Hits
argument_list|()
decl_stmt|;
name|int
name|numberOfHits
init|=
name|doSearch
argument_list|(
name|searchPhrase
argument_list|,
name|maxNumberOfHits
argument_list|,
name|luenceVersion
argument_list|)
decl_stmt|;
name|searchHits
operator|.
name|setNumberOfHits
argument_list|(
name|numberOfHits
argument_list|)
expr_stmt|;
for|for
control|(
name|ScoreDoc
name|hit
range|:
name|hits
control|)
block|{
name|Document
name|selectedDocument
init|=
name|indexSearcher
operator|.
name|doc
argument_list|(
name|hit
operator|.
name|doc
argument_list|)
decl_stmt|;
name|Hit
name|aHit
init|=
operator|new
name|Hit
argument_list|()
decl_stmt|;
name|aHit
operator|.
name|setHitLocation
argument_list|(
name|hit
operator|.
name|doc
argument_list|)
expr_stmt|;
name|aHit
operator|.
name|setScore
argument_list|(
name|hit
operator|.
name|score
argument_list|)
expr_stmt|;
name|aHit
operator|.
name|setData
argument_list|(
name|selectedDocument
operator|.
name|get
argument_list|(
literal|"contents"
argument_list|)
argument_list|)
expr_stmt|;
name|searchHits
operator|.
name|getHit
argument_list|()
operator|.
name|add
argument_list|(
name|aHit
argument_list|)
expr_stmt|;
block|}
return|return
name|searchHits
return|;
block|}
DECL|method|doSearch (String searchPhrase, int maxNumberOfHits, Version luenceVersion)
specifier|private
name|int
name|doSearch
parameter_list|(
name|String
name|searchPhrase
parameter_list|,
name|int
name|maxNumberOfHits
parameter_list|,
name|Version
name|luenceVersion
parameter_list|)
throws|throws
name|NullPointerException
throws|,
name|ParseException
throws|,
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
literal|"*** Search Phrase: "
operator|+
name|searchPhrase
operator|+
literal|" ***"
argument_list|)
expr_stmt|;
block|}
name|QueryParser
name|parser
init|=
operator|new
name|QueryParser
argument_list|(
name|luenceVersion
argument_list|,
literal|"contents"
argument_list|,
name|analyzer
argument_list|)
decl_stmt|;
name|Query
name|query
init|=
name|parser
operator|.
name|parse
argument_list|(
name|searchPhrase
argument_list|)
decl_stmt|;
name|TopScoreDocCollector
name|collector
init|=
name|TopScoreDocCollector
operator|.
name|create
argument_list|(
name|maxNumberOfHits
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|indexSearcher
operator|.
name|search
argument_list|(
name|query
argument_list|,
name|collector
argument_list|)
expr_stmt|;
name|hits
operator|=
name|collector
operator|.
name|topDocs
argument_list|()
operator|.
name|scoreDocs
expr_stmt|;
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
literal|"*** Search generated "
operator|+
name|hits
operator|.
name|length
operator|+
literal|" hits ***"
argument_list|)
expr_stmt|;
block|}
return|return
name|hits
operator|.
name|length
return|;
block|}
block|}
end_class

end_unit

