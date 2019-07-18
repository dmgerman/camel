begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|camel
operator|.
name|support
operator|.
name|DefaultProducer
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

begin_class
DECL|class|LuceneQueryProducer
specifier|public
class|class
name|LuceneQueryProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|config
name|LuceneConfiguration
name|config
decl_stmt|;
DECL|field|searcher
name|LuceneSearcher
name|searcher
decl_stmt|;
DECL|field|analyzer
name|Analyzer
name|analyzer
decl_stmt|;
DECL|field|indexDirectory
name|File
name|indexDirectory
decl_stmt|;
DECL|field|maxNumberOfHits
name|int
name|maxNumberOfHits
decl_stmt|;
DECL|field|totalHitsThreshold
name|int
name|totalHitsThreshold
decl_stmt|;
DECL|method|LuceneQueryProducer (Endpoint endpoint, LuceneConfiguration config)
specifier|public
name|LuceneQueryProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|LuceneConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|indexDirectory
operator|=
name|config
operator|.
name|getIndexDirectory
argument_list|()
expr_stmt|;
name|analyzer
operator|=
name|config
operator|.
name|getAnalyzer
argument_list|()
expr_stmt|;
name|maxNumberOfHits
operator|=
name|config
operator|.
name|getMaxHits
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|searcher
operator|=
operator|new
name|LuceneSearcher
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|searcher
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Hits
name|hits
decl_stmt|;
name|String
name|phrase
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"QUERY"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|returnLuceneDocs
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"RETURN_LUCENE_DOCS"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|isReturnLuceneDocs
init|=
operator|(
name|returnLuceneDocs
operator|!=
literal|null
operator|&&
name|returnLuceneDocs
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"true"
argument_list|)
operator|)
condition|?
literal|true
else|:
literal|false
decl_stmt|;
if|if
condition|(
name|phrase
operator|!=
literal|null
condition|)
block|{
name|searcher
operator|.
name|open
argument_list|(
name|indexDirectory
argument_list|,
name|analyzer
argument_list|)
expr_stmt|;
name|hits
operator|=
name|searcher
operator|.
name|search
argument_list|(
name|phrase
argument_list|,
name|maxNumberOfHits
argument_list|,
name|totalHitsThreshold
argument_list|,
name|config
operator|.
name|getLuceneVersion
argument_list|()
argument_list|,
name|isReturnLuceneDocs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"SearchPhrase for LucenePhraseQuerySearcher not set. Set the Header value: QUERY"
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|hits
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|LuceneConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setConfig (LuceneConfiguration config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|LuceneConfiguration
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
block|}
end_class

end_unit

