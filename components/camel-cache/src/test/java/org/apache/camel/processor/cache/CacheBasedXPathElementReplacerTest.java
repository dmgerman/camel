begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|cache
operator|.
name|CacheConstants
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
DECL|class|CacheBasedXPathElementReplacerTest
specifier|public
class|class
name|CacheBasedXPathElementReplacerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CacheBasedXPathElementReplacerTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:loadcache"
argument_list|)
DECL|field|producerTemplate
specifier|protected
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
DECL|field|xmlFragment
name|String
name|xmlFragment
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
operator|+
literal|"<books>"
operator|+
literal|"<book1>"
operator|+
literal|"</book1>"
operator|+
literal|"<book2>"
operator|+
literal|"</book2>"
operator|+
literal|"</books>"
decl_stmt|;
DECL|field|book1
name|String
name|book1
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
operator|+
literal|"<book1>"
operator|+
literal|"<novel>My Man Jeeves</novel>"
operator|+
literal|"<author>P.G Wodehouse</author>"
operator|+
literal|"</book1>"
decl_stmt|;
DECL|field|book2
name|String
name|book2
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
operator|+
literal|"<book2>"
operator|+
literal|"<novel>The Jungle Book</novel>"
operator|+
literal|"<author>Rudyard Kipling</author>"
operator|+
literal|"</book2>"
decl_stmt|;
annotation|@
name|Test
DECL|method|testCacheBasedXPathElementReplacer ()
specifier|public
name|void
name|testCacheBasedXPathElementReplacer
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Beginning Test ---> testCacheBasedXPathElementReplacer()"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"book1"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"book2"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"XML_FRAGMENT"
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|String
name|key
range|:
name|keys
control|)
block|{
name|producerTemplate
operator|.
name|send
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
name|CacheConstants
operator|.
name|CACHE_OPERATION
argument_list|,
name|CacheConstants
operator|.
name|CACHE_OPERATION_ADD
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|key
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"book1"
argument_list|)
condition|)
block|{
name|in
operator|.
name|setBody
argument_list|(
name|book1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"book2"
argument_list|)
condition|)
block|{
name|in
operator|.
name|setBody
argument_list|(
name|book2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|in
operator|.
name|setBody
argument_list|(
name|xmlFragment
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Completed Test ---> testCacheBasedXPathElementReplacer()"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
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
literal|"cache://TestCache1"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"XML_FRAGMENT"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|CacheBasedXPathReplacer
argument_list|(
literal|"cache://TestCache1"
argument_list|,
literal|"book1"
argument_list|,
literal|"/books/book1"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|CacheBasedXPathReplacer
argument_list|(
literal|"cache://TestCache1"
argument_list|,
literal|"book2"
argument_list|,
literal|"/books/book2"
argument_list|)
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
name|String
name|key
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|String
name|data
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"------- Payload Replacement Results ---------"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"The following Payload was replaced from Cache: TestCache1"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"key = "
operator|+
name|key
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Before Value = "
operator|+
name|xmlFragment
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"After value = "
operator|+
name|data
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"------ End  ------"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:loadcache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"cache://TestCache1"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

