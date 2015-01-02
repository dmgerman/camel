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
name|context
operator|.
name|annotation
operator|.
name|Configuration
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
name|ConversionService
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
name|support
operator|.
name|DefaultConversionService
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|ConversionServiceConfig
operator|.
name|providedConversionService
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
name|ConversionServiceConfig
operator|.
name|class
argument_list|)
annotation|@
name|IntegrationTest
DECL|class|ExistingConversionServiceTest
specifier|public
class|class
name|ExistingConversionServiceTest
extends|extends
name|Assert
block|{
annotation|@
name|Autowired
DECL|field|conversionService
name|ConversionService
name|conversionService
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldUseProvidedConversionService ()
specifier|public
name|void
name|shouldUseProvidedConversionService
parameter_list|()
block|{
name|assertSame
argument_list|(
name|providedConversionService
argument_list|,
name|conversionService
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_class
annotation|@
name|Configuration
DECL|class|ConversionServiceConfig
class|class
name|ConversionServiceConfig
block|{
DECL|field|providedConversionService
specifier|static
name|ConversionService
name|providedConversionService
init|=
operator|new
name|DefaultConversionService
argument_list|()
decl_stmt|;
annotation|@
name|Bean
DECL|method|conversionService ()
name|ConversionService
name|conversionService
parameter_list|()
block|{
return|return
name|providedConversionService
return|;
block|}
block|}
end_class

end_unit

