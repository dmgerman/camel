begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.lucene
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|lucene
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
DECL|class|LuceneQueryProcessorTest
specifier|public
class|class
name|LuceneQueryProcessorTest
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
name|LuceneQueryProcessorTest
operator|.
name|class
argument_list|)
decl_stmt|;
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
DECL|method|sendRequest ()
specifier|private
name|void
name|sendRequest
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
DECL|method|testPhraseSearcher ()
specifier|public
name|void
name|testPhraseSearcher
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|StandardAnalyzer
name|analyzer
init|=
operator|new
name|StandardAnalyzer
argument_list|(
name|Version
operator|.
name|LUCENE_46
argument_list|)
decl_stmt|;
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
try|try
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
literal|"Rodney Dangerfield"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|LuceneQueryProcessor
argument_list|(
literal|"target/stdindexDir"
argument_list|,
name|analyzer
argument_list|,
literal|null
argument_list|,
literal|20
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:next"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
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
literal|"------------Beginning Phrase + Standard Analyzer Search Test---------------"
argument_list|)
expr_stmt|;
name|sendRequest
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
literal|"------------Completed Phrase + Standard Analyzer Search Test---------------"
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
DECL|method|testWildcardSearcher ()
specifier|public
name|void
name|testWildcardSearcher
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|WhitespaceAnalyzer
name|analyzer
init|=
operator|new
name|WhitespaceAnalyzer
argument_list|(
name|Version
operator|.
name|LUCENE_46
argument_list|)
decl_stmt|;
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
try|try
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
literal|"Carl*"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|LuceneQueryProcessor
argument_list|(
literal|"target/simpleindexDir"
argument_list|,
name|analyzer
argument_list|,
literal|null
argument_list|,
literal|20
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:next"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
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
literal|"------------Beginning Wildcard + Simple Analyzer Phrase Searcher Test---------------"
argument_list|)
expr_stmt|;
name|sendRequest
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
literal|"------------Completed Wildcard + Simple Analyzer Phrase Searcher Test---------------"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

