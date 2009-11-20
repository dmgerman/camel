begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.simple.onetomany
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|simple
operator|.
name|onetomany
package|;
end_package

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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|CsvRecord
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|DataField
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|OneToMany
import|;
end_import

begin_class
annotation|@
name|CsvRecord
argument_list|(
name|separator
operator|=
literal|","
argument_list|)
DECL|class|Author
specifier|public
class|class
name|Author
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|)
DECL|field|firstName
specifier|private
name|String
name|firstName
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
argument_list|)
DECL|field|lastName
specifier|private
name|String
name|lastName
decl_stmt|;
annotation|@
name|OneToMany
DECL|field|books
specifier|private
name|List
argument_list|<
name|Book
argument_list|>
name|books
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|5
argument_list|)
DECL|field|age
specifier|private
name|String
name|age
decl_stmt|;
DECL|method|getAge ()
specifier|public
name|String
name|getAge
parameter_list|()
block|{
return|return
name|age
return|;
block|}
DECL|method|setAge (String age)
specifier|public
name|void
name|setAge
parameter_list|(
name|String
name|age
parameter_list|)
block|{
name|this
operator|.
name|age
operator|=
name|age
expr_stmt|;
block|}
DECL|method|getBooks ()
specifier|public
name|List
argument_list|<
name|Book
argument_list|>
name|getBooks
parameter_list|()
block|{
return|return
name|books
return|;
block|}
DECL|method|setBooks (List<Book> books)
specifier|public
name|void
name|setBooks
parameter_list|(
name|List
argument_list|<
name|Book
argument_list|>
name|books
parameter_list|)
block|{
name|this
operator|.
name|books
operator|=
name|books
expr_stmt|;
block|}
DECL|method|getFirstName ()
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
DECL|method|setFirstName (String firstName)
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|firstName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
block|}
DECL|method|getLastName ()
specifier|public
name|String
name|getLastName
parameter_list|()
block|{
return|return
name|lastName
return|;
block|}
DECL|method|setLastName (String lastName)
specifier|public
name|void
name|setLastName
parameter_list|(
name|String
name|lastName
parameter_list|)
block|{
name|this
operator|.
name|lastName
operator|=
name|lastName
expr_stmt|;
block|}
block|}
end_class

end_unit

