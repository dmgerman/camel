begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.processor.scattergather
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|scattergather
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
name|language
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_comment
comment|//START SNIPPET: e1
end_comment

begin_class
DECL|class|MyVendor
specifier|public
class|class
name|MyVendor
block|{
DECL|field|beerPrice
specifier|private
name|int
name|beerPrice
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"seda:quoteAggregator"
argument_list|)
DECL|field|quoteAggregator
specifier|private
name|ProducerTemplate
name|quoteAggregator
decl_stmt|;
DECL|method|MyVendor (int beerPrice)
specifier|public
name|MyVendor
parameter_list|(
name|int
name|beerPrice
parameter_list|)
block|{
name|this
operator|.
name|beerPrice
operator|=
name|beerPrice
expr_stmt|;
block|}
DECL|method|getQuote (@PathR) String item, Exchange exchange)
specifier|public
name|void
name|getQuote
parameter_list|(
annotation|@
name|XPath
argument_list|(
literal|"/quote_request/@item"
argument_list|)
name|String
name|item
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
literal|"beer"
operator|.
name|equals
argument_list|(
name|item
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|beerPrice
argument_list|)
expr_stmt|;
name|quoteAggregator
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"No quote available for "
operator|+
name|item
argument_list|)
throw|;
block|}
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: e1
end_comment

end_unit

