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
name|Map
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
name|Message
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
name|Processor
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
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|spi
operator|.
name|Registry
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|core
operator|.
name|SimpleAnalyzer
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
name|core
operator|.
name|WhitespaceAnalyzer
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
name|junit
operator|.
name|Test
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
DECL|class|LuceneIndexAndQueryProducerTest
specifier|public
class|class
name|LuceneIndexAndQueryProducerTest
extends|extends
name|CamelTestSupport
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
name|LuceneIndexAndQueryProducerTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|humorousQuotes
specifier|private
name|String
index|[]
name|humorousQuotes
init|=
block|{
literal|"I think, therefore I am. I think - George Carlin"
block|,
literal|"I have as much authority as the Pope. I just don't have as many people who believe it. - George Carlin"
block|,
literal|"There`s no present. There`s only the immediate future and the recent past - George Carlin"
block|,
literal|"Politics doesn't make strange bedfellows - marriage does. - Groucho Marx"
block|,
literal|"I refuse to join any club that would have me as a member. - Groucho Marx"
block|,
literal|"I tell ya when I was a kid, all I knew was rejection. My yo-yo, it never came back. - Rodney Dangerfield"
block|,
literal|"I worked in a pet store and people kept asking how big I'd get. - Rodney Dangerfield"
block|}
decl_stmt|;
annotation|@
name|Override
DECL|method|bindToRegistry (Registry registry)
specifier|protected
name|void
name|bindToRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
name|registry
operator|.
name|bind
argument_list|(
literal|"std"
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/stdindexDir"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"load_dir"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/sources"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"stdAnalyzer"
argument_list|,
operator|new
name|StandardAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"simple"
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/simpleindexDir"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"simpleAnalyzer"
argument_list|,
operator|new
name|SimpleAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"whitespace"
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/whitespaceindexDir"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"whitespaceAnalyzer"
argument_list|,
operator|new
name|WhitespaceAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|sendRequest (final String quote)
specifier|private
name|void
name|sendRequest
parameter_list|(
specifier|final
name|String
name|quote
parameter_list|)
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
comment|// Set the property of the charset encoding
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|quote
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|sendQuery ()
specifier|private
name|void
name|sendQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
comment|// Set the property of the charset encoding
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"QUERY"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLuceneIndexProducer ()
specifier|public
name|void
name|testLuceneIndexProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"lucene:stdQuotesIndex:insert?analyzer=#stdAnalyzer&indexDir=#std&srcDir=#load_dir"
argument_list|)
operator|.
name|to
argument_list|(
literal|"lucene:simpleQuotesIndex:insert?analyzer=#simpleAnalyzer&indexDir=#simple&srcDir=#load_dir"
argument_list|)
operator|.
name|to
argument_list|(
literal|"lucene:whitespaceQuotesIndex:insert?analyzer=#whitespaceAnalyzer&indexDir=#whitespace&srcDir=#load_dir"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"------------Beginning LuceneIndexProducer Test---------------"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|quote
range|:
name|humorousQuotes
control|)
block|{
name|sendRequest
argument_list|(
name|quote
argument_list|)
expr_stmt|;
block|}
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"------------Completed LuceneIndexProducer Test---------------"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLucenePhraseQueryProducer ()
specifier|public
name|void
name|testLucenePhraseQueryProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockSearchEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:searchResult"
argument_list|)
decl_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"QUERY"
argument_list|,
name|constant
argument_list|(
literal|"Seinfeld"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"lucene:searchIndex:query?analyzer=#whitespaceAnalyzer&indexDir=#whitespace&maxHits=20"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:next"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:next"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Hits
operator|.
name|class
argument_list|)
decl_stmt|;
name|printResults
argument_list|(
name|hits
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|printResults
parameter_list|(
name|Hits
name|hits
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Number of hits: "
operator|+
name|hits
operator|.
name|getNumberOfHits
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hits
operator|.
name|getNumberOfHits
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Hit "
operator|+
name|i
operator|+
literal|" Index Location:"
operator|+
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getHitLocation
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Hit "
operator|+
name|i
operator|+
literal|" Score:"
operator|+
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getScore
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Hit "
operator|+
name|i
operator|+
literal|" Data:"
operator|+
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:searchResult"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"------------Beginning LuceneQueryProducer Phrase Test---------------"
argument_list|)
expr_stmt|;
name|sendQuery
argument_list|()
expr_stmt|;
name|mockSearchEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"------------Completed LuceneQueryProducer Phrase Test---------------"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLuceneWildcardQueryProducer ()
specifier|public
name|void
name|testLuceneWildcardQueryProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockSearchEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:searchResult"
argument_list|)
decl_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"QUERY"
argument_list|,
name|constant
argument_list|(
literal|"Grouc?? Marx"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"lucene:searchIndex:query?analyzer=#stdAnalyzer&indexDir=#std&maxHits=20"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:next"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:next"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Hits
operator|.
name|class
argument_list|)
decl_stmt|;
name|printResults
argument_list|(
name|hits
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|printResults
parameter_list|(
name|Hits
name|hits
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Number of hits: "
operator|+
name|hits
operator|.
name|getNumberOfHits
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hits
operator|.
name|getNumberOfHits
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Hit "
operator|+
name|i
operator|+
literal|" Index Location:"
operator|+
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getHitLocation
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Hit "
operator|+
name|i
operator|+
literal|" Score:"
operator|+
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getScore
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Hit "
operator|+
name|i
operator|+
literal|" Data:"
operator|+
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:searchResult"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"------------Beginning  LuceneQueryProducer Wildcard Test---------------"
argument_list|)
expr_stmt|;
name|sendQuery
argument_list|()
expr_stmt|;
name|mockSearchEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"------------Completed LuceneQueryProducer Wildcard Test---------------"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReturnLuceneDocsQueryProducer ()
specifier|public
name|void
name|testReturnLuceneDocsQueryProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockSearchEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:searchResult"
argument_list|)
decl_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"QUERY"
argument_list|,
name|constant
argument_list|(
literal|"Grouc?? Marx"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"RETURN_LUCENE_DOCS"
argument_list|,
name|constant
argument_list|(
literal|"true"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"lucene:searchIndex:query?analyzer=#stdAnalyzer&indexDir=#std&maxHits=20"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:next"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:next"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Hits
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|printResults
argument_list|(
name|hits
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|printResults
parameter_list|(
name|Hits
name|hits
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Number of hits: "
operator|+
name|hits
operator|.
name|getNumberOfHits
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hits
operator|.
name|getNumberOfHits
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Hit "
operator|+
name|i
operator|+
literal|" Index Location:"
operator|+
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getHitLocation
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Hit "
operator|+
name|i
operator|+
literal|" Score:"
operator|+
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getScore
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Hit "
operator|+
name|i
operator|+
literal|" Data:"
operator|+
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|hits
operator|.
name|getHit
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getDocument
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Failed to return lucene documents"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:searchResult"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Hits
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|hits
operator|==
literal|null
condition|)
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"NO_LUCENE_DOCS_ERROR"
argument_list|,
literal|"NO LUCENE DOCS FOUND"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|setGlobalOptions
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Number of hits: "
operator|+
name|hits
operator|.
name|getNumberOfHits
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"------------Beginning  LuceneQueryProducer Wildcard with Return Lucene Docs Test---------------"
argument_list|)
expr_stmt|;
name|sendQuery
argument_list|()
expr_stmt|;
name|mockSearchEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|errorMap
init|=
name|mockSearchEndpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getGlobalOptions
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"------------Completed LuceneQueryProducer Wildcard with Return Lucene Docs Test---------------"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|errorMap
operator|.
name|get
argument_list|(
literal|"NO_LUCENE_DOCS_ERROR"
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

