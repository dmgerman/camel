begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jooq.beans
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jooq
operator|.
name|beans
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
name|component
operator|.
name|jooq
operator|.
name|db
operator|.
name|tables
operator|.
name|records
operator|.
name|BookStoreRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jooq
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jooq
operator|.
name|ResultQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jooq
operator|.
name|impl
operator|.
name|DSL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jooq
operator|.
name|db
operator|.
name|Tables
operator|.
name|BOOK_STORE
import|;
end_import

begin_class
annotation|@
name|Component
DECL|class|BookStoreRecordBean
specifier|public
class|class
name|BookStoreRecordBean
block|{
DECL|field|name
specifier|private
name|String
name|name
init|=
literal|"test"
decl_stmt|;
DECL|method|generate ()
specifier|public
name|BookStoreRecord
name|generate
parameter_list|()
block|{
return|return
operator|new
name|BookStoreRecord
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|select ()
specifier|public
name|ResultQuery
name|select
parameter_list|()
block|{
return|return
name|DSL
operator|.
name|selectFrom
argument_list|(
name|BOOK_STORE
argument_list|)
operator|.
name|where
argument_list|(
name|BOOK_STORE
operator|.
name|NAME
operator|.
name|eq
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|delete ()
specifier|public
name|Query
name|delete
parameter_list|()
block|{
return|return
name|DSL
operator|.
name|delete
argument_list|(
name|BOOK_STORE
argument_list|)
operator|.
name|where
argument_list|(
name|BOOK_STORE
operator|.
name|NAME
operator|.
name|eq
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

