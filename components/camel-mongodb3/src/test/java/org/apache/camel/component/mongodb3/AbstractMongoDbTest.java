begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb3
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb3
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Formatter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|MongoClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|MongoCollection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|MongoDatabase
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
name|CamelContext
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
name|CamelExecutionException
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
name|properties
operator|.
name|PropertiesComponent
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
name|spring
operator|.
name|SpringCamelContext
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
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|AnnotationConfigApplicationContext
import|;
end_import

begin_class
DECL|class|AbstractMongoDbTest
specifier|public
specifier|abstract
class|class
name|AbstractMongoDbTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|mongo
specifier|protected
specifier|static
name|MongoClient
name|mongo
decl_stmt|;
DECL|field|db
specifier|protected
specifier|static
name|MongoDatabase
name|db
decl_stmt|;
DECL|field|testCollection
specifier|protected
specifier|static
name|MongoCollection
argument_list|<
name|Document
argument_list|>
name|testCollection
decl_stmt|;
DECL|field|dynamicCollection
specifier|protected
specifier|static
name|MongoCollection
argument_list|<
name|Document
argument_list|>
name|dynamicCollection
decl_stmt|;
DECL|field|dbName
specifier|protected
specifier|static
name|String
name|dbName
init|=
literal|"test"
decl_stmt|;
DECL|field|testCollectionName
specifier|protected
specifier|static
name|String
name|testCollectionName
decl_stmt|;
DECL|field|dynamicCollectionName
specifier|protected
specifier|static
name|String
name|dynamicCollectionName
decl_stmt|;
DECL|field|applicationContext
specifier|protected
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Override
DECL|method|doPostSetup ()
specifier|public
name|void
name|doPostSetup
parameter_list|()
block|{
name|mongo
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"myDb"
argument_list|,
name|MongoClient
operator|.
name|class
argument_list|)
expr_stmt|;
name|db
operator|=
name|mongo
operator|.
name|getDatabase
argument_list|(
name|dbName
argument_list|)
expr_stmt|;
comment|// Refresh the test collection - drop it and recreate it. We don't do
comment|// this for the database because MongoDB would create large
comment|// store files each time
name|testCollectionName
operator|=
literal|"camelTest"
expr_stmt|;
name|testCollection
operator|=
name|db
operator|.
name|getCollection
argument_list|(
name|testCollectionName
argument_list|,
name|Document
operator|.
name|class
argument_list|)
expr_stmt|;
name|testCollection
operator|.
name|drop
argument_list|()
expr_stmt|;
name|testCollection
operator|=
name|db
operator|.
name|getCollection
argument_list|(
name|testCollectionName
argument_list|,
name|Document
operator|.
name|class
argument_list|)
expr_stmt|;
name|dynamicCollectionName
operator|=
name|testCollectionName
operator|.
name|concat
argument_list|(
literal|"Dynamic"
argument_list|)
expr_stmt|;
name|dynamicCollection
operator|=
name|db
operator|.
name|getCollection
argument_list|(
name|dynamicCollectionName
argument_list|,
name|Document
operator|.
name|class
argument_list|)
expr_stmt|;
name|dynamicCollection
operator|.
name|drop
argument_list|()
expr_stmt|;
name|dynamicCollection
operator|=
name|db
operator|.
name|getCollection
argument_list|(
name|dynamicCollectionName
argument_list|,
name|Document
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|testCollection
operator|.
name|drop
argument_list|()
expr_stmt|;
name|dynamicCollection
operator|.
name|drop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|=
operator|new
name|AnnotationConfigApplicationContext
argument_list|(
name|EmbedMongoConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
name|CamelContext
name|ctx
init|=
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
decl_stmt|;
name|PropertiesComponent
name|pc
init|=
operator|new
name|PropertiesComponent
argument_list|(
literal|"classpath:mongodb.test.properties"
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
name|pc
argument_list|)
expr_stmt|;
return|return
name|ctx
return|;
block|}
DECL|method|pumpDataIntoTestCollection ()
specifier|protected
name|void
name|pumpDataIntoTestCollection
parameter_list|()
block|{
comment|// there should be 100 of each
name|String
index|[]
name|scientists
init|=
block|{
literal|"Einstein"
block|,
literal|"Darwin"
block|,
literal|"Copernicus"
block|,
literal|"Pasteur"
block|,
literal|"Curie"
block|,
literal|"Faraday"
block|,
literal|"Newton"
block|,
literal|"Bohr"
block|,
literal|"Galilei"
block|,
literal|"Maxwell"
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|int
name|index
init|=
name|i
operator|%
name|scientists
operator|.
name|length
decl_stmt|;
name|Formatter
name|f
init|=
operator|new
name|Formatter
argument_list|()
decl_stmt|;
name|String
name|doc
init|=
name|f
operator|.
name|format
argument_list|(
literal|"{\"_id\":\"%d\", \"scientist\":\"%s\", \"fixedField\": \"fixedValue\"}"
argument_list|,
name|i
argument_list|,
name|scientists
index|[
name|index
index|]
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|testCollection
operator|.
name|insertOne
argument_list|(
name|Document
operator|.
name|parse
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Data pumping of 1000 entries did not complete entirely"
argument_list|,
literal|1000L
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|extractAndAssertCamelMongoDbException (Object result, String message)
specifier|protected
name|CamelMongoDbException
name|extractAndAssertCamelMongoDbException
parameter_list|(
name|Object
name|result
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Result is not an Exception"
argument_list|,
name|result
operator|instanceof
name|Throwable
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not an CamelExecutionException"
argument_list|,
name|result
operator|instanceof
name|CamelExecutionException
argument_list|)
expr_stmt|;
name|Throwable
name|exc
init|=
operator|(
operator|(
name|CamelExecutionException
operator|)
name|result
operator|)
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not an CamelMongoDbException"
argument_list|,
name|exc
operator|instanceof
name|CamelMongoDbException
argument_list|)
expr_stmt|;
name|CamelMongoDbException
name|camelExc
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|CamelMongoDbException
operator|.
name|class
argument_list|,
name|exc
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
literal|"CamelMongoDbException doesn't contain desired message string"
argument_list|,
name|camelExc
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|camelExc
return|;
block|}
block|}
end_class

end_unit

