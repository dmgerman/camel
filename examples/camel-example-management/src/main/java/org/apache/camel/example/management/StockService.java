begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|management
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
name|Random
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|XPath
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|StockService
specifier|public
class|class
name|StockService
block|{
DECL|field|symbols
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|symbols
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|stat
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|stat
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|StockService ()
specifier|public
name|StockService
parameter_list|()
block|{
name|symbols
operator|.
name|add
argument_list|(
literal|"IBM"
argument_list|)
expr_stmt|;
name|symbols
operator|.
name|add
argument_list|(
literal|"APPLE"
argument_list|)
expr_stmt|;
name|symbols
operator|.
name|add
argument_list|(
literal|"ORCL"
argument_list|)
expr_stmt|;
block|}
DECL|method|transform (@PathR) String symbol, @XPath(R) String value)
specifier|public
name|String
name|transform
parameter_list|(
annotation|@
name|XPath
argument_list|(
literal|"/stock/symbol/text()"
argument_list|)
name|String
name|symbol
parameter_list|,
annotation|@
name|XPath
argument_list|(
literal|"/stock/value/text()"
argument_list|)
name|String
name|value
parameter_list|)
block|{
name|Integer
name|hits
init|=
name|stat
operator|.
name|get
argument_list|(
name|symbol
argument_list|)
decl_stmt|;
if|if
condition|(
name|hits
operator|==
literal|null
condition|)
block|{
name|hits
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|hits
operator|++
expr_stmt|;
block|}
name|stat
operator|.
name|put
argument_list|(
name|symbol
argument_list|,
name|hits
argument_list|)
expr_stmt|;
return|return
name|symbol
operator|+
literal|"@"
operator|+
name|hits
return|;
block|}
DECL|method|getHits ()
specifier|public
name|String
name|getHits
parameter_list|()
block|{
return|return
name|stat
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|createRandomStocks ()
specifier|public
name|String
name|createRandomStocks
parameter_list|()
block|{
name|Random
name|ran
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|StringBuilder
name|xml
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|xml
operator|.
name|append
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
argument_list|)
expr_stmt|;
name|xml
operator|.
name|append
argument_list|(
literal|"<stocks>\n"
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
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|int
name|winner
init|=
name|ran
operator|.
name|nextInt
argument_list|(
name|symbols
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|symbol
init|=
name|symbols
operator|.
name|get
argument_list|(
name|winner
argument_list|)
decl_stmt|;
name|int
name|value
init|=
name|ran
operator|.
name|nextInt
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|xml
operator|.
name|append
argument_list|(
literal|"<stock>"
argument_list|)
expr_stmt|;
name|xml
operator|.
name|append
argument_list|(
literal|"<symbol>"
argument_list|)
operator|.
name|append
argument_list|(
name|symbol
argument_list|)
operator|.
name|append
argument_list|(
literal|"</symbol>"
argument_list|)
expr_stmt|;
name|xml
operator|.
name|append
argument_list|(
literal|"<value>"
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|"</value>"
argument_list|)
expr_stmt|;
name|xml
operator|.
name|append
argument_list|(
literal|"</stock>\n"
argument_list|)
expr_stmt|;
block|}
name|xml
operator|.
name|append
argument_list|(
literal|"</stocks>"
argument_list|)
expr_stmt|;
return|return
name|xml
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

