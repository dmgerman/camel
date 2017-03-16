begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
package|;
end_package

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
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
operator|.
name|identity
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Value
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|ApplicationArguments
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|ApplicationRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|SpringApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
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

begin_class
annotation|@
name|SpringBootApplication
DECL|class|RestSwaggerApplication
specifier|public
class|class
name|RestSwaggerApplication
implements|implements
name|ApplicationRunner
block|{
annotation|@
name|Autowired
DECL|field|context
name|ApplicationContext
name|context
decl_stmt|;
annotation|@
name|Value
argument_list|(
literal|"${operation:getInventory}"
argument_list|)
DECL|field|operation
name|String
name|operation
decl_stmt|;
annotation|@
name|Value
argument_list|(
literal|"${swagger:http://petstore.swagger.io/v2/swagger.json}"
argument_list|)
DECL|field|specificationUri
name|String
name|specificationUri
decl_stmt|;
annotation|@
name|Autowired
DECL|field|template
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Override
DECL|method|run (final ApplicationArguments args)
specifier|public
name|void
name|run
parameter_list|(
specifier|final
name|ApplicationArguments
name|args
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Predicate
argument_list|<
name|String
argument_list|>
name|operations
init|=
literal|"operation"
operator|::
name|equals
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|args
operator|.
name|getOptionNames
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|operations
operator|.
name|negate
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|identity
argument_list|()
argument_list|,
name|arg
lambda|->
name|args
operator|.
name|getOptionValues
argument_list|(
name|arg
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|String
name|body
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"rest-swagger:"
operator|+
name|specificationUri
operator|+
literal|"#"
operator|+
name|operation
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|SpringApplication
operator|.
name|exit
argument_list|(
name|context
argument_list|,
parameter_list|()
lambda|->
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|main (final String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
block|{
name|SpringApplication
operator|.
name|run
argument_list|(
name|RestSwaggerApplication
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

