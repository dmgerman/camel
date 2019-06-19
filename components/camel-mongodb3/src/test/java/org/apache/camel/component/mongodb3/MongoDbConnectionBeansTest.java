begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Properties
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
name|NoSuchBeanException
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
name|junit
operator|.
name|Test
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
DECL|class|MongoDbConnectionBeansTest
specifier|public
class|class
name|MongoDbConnectionBeansTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
DECL|method|checkConnectionFromProperties ()
specifier|public
name|void
name|checkConnectionFromProperties
parameter_list|()
block|{
name|MongoDbEndpoint
name|testEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mongodb3:anyName?mongoConnection=#myDb&database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=count&dynamicity=true"
argument_list|,
name|MongoDbEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
literal|"myDb"
argument_list|,
name|testEndpoint
operator|.
name|getConnectionBean
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mongo
argument_list|,
name|testEndpoint
operator|.
name|getMongoConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkConnectionFromBean ()
specifier|public
name|void
name|checkConnectionFromBean
parameter_list|()
block|{
name|MongoDbEndpoint
name|testEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mongodb3:myDb?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=count&dynamicity=true"
argument_list|,
name|MongoDbEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myDb"
argument_list|,
name|testEndpoint
operator|.
name|getConnectionBean
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mongo
argument_list|,
name|testEndpoint
operator|.
name|getMongoConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkConnectionBothExisting ()
specifier|public
name|void
name|checkConnectionBothExisting
parameter_list|()
block|{
name|MongoDbEndpoint
name|testEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mongodb3:myDb?mongoConnection=#myDbS&database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=count&dynamicity=true"
argument_list|,
name|MongoDbEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myDb"
argument_list|,
name|testEndpoint
operator|.
name|getConnectionBean
argument_list|()
argument_list|)
expr_stmt|;
name|MongoClient
name|myDbS
init|=
name|mongo
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"myDbS"
argument_list|,
name|MongoClient
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|myDbS
argument_list|,
name|testEndpoint
operator|.
name|getMongoConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|Exception
operator|.
name|class
argument_list|)
DECL|method|checkMissingConnection ()
specifier|public
name|void
name|checkMissingConnection
parameter_list|()
block|{
name|MongoDbEndpoint
name|testEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mongodb3:anythingNotRelated?database={{mongodb.testDb}}&collection={{mongodb.testCollection}}&operation=count&dynamicity=true"
argument_list|,
name|MongoDbEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

