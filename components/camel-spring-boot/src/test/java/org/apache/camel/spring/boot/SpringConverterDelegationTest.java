begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
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
name|TypeConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|boot
operator|.
name|autoconfigure
operator|.
name|EnableAutoConfiguration
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
name|test
operator|.
name|IntegrationTest
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
name|test
operator|.
name|SpringApplicationConfiguration
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
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|converter
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|EnableAutoConfiguration
annotation|@
name|SpringApplicationConfiguration
argument_list|(
name|classes
operator|=
name|SpringConverterDelegationTest
operator|.
name|class
argument_list|)
annotation|@
name|IntegrationTest
DECL|class|SpringConverterDelegationTest
specifier|public
class|class
name|SpringConverterDelegationTest
extends|extends
name|Assert
block|{
annotation|@
name|Autowired
DECL|field|typeConverter
name|TypeConverter
name|typeConverter
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldConvertUsingSpringConverter ()
specifier|public
name|void
name|shouldConvertUsingSpringConverter
parameter_list|()
block|{
name|String
name|result
init|=
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|Convertable
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"converted!"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Bean
DECL|method|convertableConverter ()
name|ConvertableConverter
name|convertableConverter
parameter_list|()
block|{
return|return
operator|new
name|ConvertableConverter
argument_list|()
return|;
block|}
block|}
end_class

begin_class
DECL|class|Convertable
class|class
name|Convertable
block|{ }
end_class

begin_class
DECL|class|ConvertableConverter
class|class
name|ConvertableConverter
implements|implements
name|Converter
argument_list|<
name|Convertable
argument_list|,
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|convert (Convertable source)
specifier|public
name|String
name|convert
parameter_list|(
name|Convertable
name|source
parameter_list|)
block|{
return|return
literal|"converted!"
return|;
block|}
block|}
end_class

end_unit

