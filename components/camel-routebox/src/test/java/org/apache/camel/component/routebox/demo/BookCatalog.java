begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox.demo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
operator|.
name|demo
package|;
end_package

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
name|Set
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
DECL|class|BookCatalog
specifier|public
class|class
name|BookCatalog
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
name|BookCatalog
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|map
specifier|private
name|HashMap
argument_list|<
name|String
argument_list|,
name|Book
argument_list|>
name|map
decl_stmt|;
DECL|method|BookCatalog ()
specifier|public
name|BookCatalog
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|map
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Book
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|BookCatalog (HashMap<String, Book> map)
specifier|public
name|BookCatalog
parameter_list|(
name|HashMap
argument_list|<
name|String
argument_list|,
name|Book
argument_list|>
name|map
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
DECL|method|addToCatalog (Book book)
specifier|public
name|String
name|addToCatalog
parameter_list|(
name|Book
name|book
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding book with author {} and title {}"
argument_list|,
name|book
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|book
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|book
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|book
argument_list|)
expr_stmt|;
return|return
literal|"Book with Author "
operator|+
name|book
operator|.
name|getAuthor
argument_list|()
operator|+
literal|" and title "
operator|+
name|book
operator|.
name|getTitle
argument_list|()
operator|+
literal|" added to Catalog"
return|;
block|}
DECL|method|findBook (String author)
specifier|public
name|Book
name|findBook
parameter_list|(
name|String
name|author
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Finding book with author {}"
argument_list|,
name|author
argument_list|)
expr_stmt|;
return|return
name|map
operator|.
name|get
argument_list|(
name|author
argument_list|)
return|;
block|}
DECL|method|findAuthor (String title)
specifier|public
name|Book
name|findAuthor
parameter_list|(
name|String
name|title
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Finding author with book {}"
argument_list|,
name|title
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|map
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|Book
name|book
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
if|if
condition|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getTitle
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|title
argument_list|)
condition|)
block|{
name|book
operator|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|book
return|;
block|}
DECL|method|initialize ()
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|Exception
block|{
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|map
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

