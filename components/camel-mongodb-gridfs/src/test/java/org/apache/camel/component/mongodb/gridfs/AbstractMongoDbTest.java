begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb.gridfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
operator|.
name|gridfs
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|gridfs
operator|.
name|GridFS
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
name|MongoClient
name|mongo
decl_stmt|;
DECL|field|gridfs
specifier|protected
name|GridFS
name|gridfs
decl_stmt|;
DECL|field|applicationContext
specifier|protected
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
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
name|MongoClient
operator|.
name|class
argument_list|)
expr_stmt|;
name|gridfs
operator|=
operator|new
name|GridFS
argument_list|(
name|mongo
operator|.
name|getDB
argument_list|(
literal|"test"
argument_list|)
argument_list|,
name|getBucket
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getBucket ()
specifier|public
name|String
name|getBucket
parameter_list|()
block|{
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|mongo
operator|.
name|close
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
name|CamelContext
name|ctx
init|=
operator|new
name|SpringCamelContext
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
block|}
end_class

end_unit

