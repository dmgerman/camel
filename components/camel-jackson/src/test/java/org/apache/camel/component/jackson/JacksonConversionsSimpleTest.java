begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jackson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
operator|.
name|converter
operator|.
name|JacksonTypeConverters
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

begin_class
DECL|class|JacksonConversionsSimpleTest
specifier|public
class|class
name|JacksonConversionsSimpleTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|shouldNotConvertMapToString ()
specifier|public
name|void
name|shouldNotConvertMapToString
parameter_list|()
block|{
name|Object
name|convertedObject
init|=
name|JacksonTypeConverters
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|convertedObject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotConvertMapToNumber ()
specifier|public
name|void
name|shouldNotConvertMapToNumber
parameter_list|()
block|{
name|Object
name|convertedObject
init|=
name|JacksonTypeConverters
operator|.
name|convertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|convertedObject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotConvertMapToPrimitive ()
specifier|public
name|void
name|shouldNotConvertMapToPrimitive
parameter_list|()
block|{
name|Object
name|convertedObject
init|=
name|JacksonTypeConverters
operator|.
name|convertTo
argument_list|(
name|long
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|convertedObject
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

