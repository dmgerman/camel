begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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
name|component
operator|.
name|consul
operator|.
name|ConsulRegistry
operator|.
name|ConsulRegistryUtils
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * @author<a href="https://github.com/ppalaga">Peter Palaga</a>  */
end_comment

begin_class
DECL|class|ConsulRegistryUtilsTest
specifier|public
class|class
name|ConsulRegistryUtilsTest
block|{
annotation|@
name|Test
DECL|method|encodeDecode ()
specifier|public
name|void
name|encodeDecode
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|src
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"one"
argument_list|,
literal|"\u0434\u0432\u0430"
argument_list|,
literal|"t\u0159i"
argument_list|)
decl_stmt|;
specifier|final
name|byte
index|[]
name|serialized
init|=
name|ConsulRegistryUtils
operator|.
name|serialize
argument_list|(
operator|(
name|Serializable
operator|)
name|src
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|src
argument_list|,
name|ConsulRegistryUtils
operator|.
name|deserialize
argument_list|(
name|serialized
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|String
name|encoded
init|=
name|ConsulRegistryUtils
operator|.
name|encodeBase64
argument_list|(
name|serialized
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"rO0ABXNyABpqYXZhLnV0aWwuQXJyYXlzJEFycmF5TGlzdNmkPL7NiAbSAgABWwABYXQAE1tMamF2YS9sYW5nL09iamVjdDt4"
operator|+
literal|"cHVyABNbTGphdmEubGFuZy5TdHJpbmc7rdJW5+kde0cCAAB4cAAAAAN0AANvbmV0AAbQtNCy0LB0AAR0xZlp"
argument_list|,
name|encoded
argument_list|)
expr_stmt|;
specifier|final
name|byte
index|[]
name|decoded
init|=
name|ConsulRegistryUtils
operator|.
name|decodeBase64
argument_list|(
name|encoded
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|-
literal|84
block|,
operator|-
literal|19
block|,
literal|0
block|,
literal|5
block|,
literal|115
block|,
literal|114
block|,
literal|0
block|,
literal|26
block|,
literal|106
block|,
literal|97
block|,
literal|118
block|,
literal|97
block|,
literal|46
block|,
literal|117
block|,
literal|116
block|,
literal|105
block|,
literal|108
block|,
literal|46
block|,
literal|65
block|,
literal|114
block|,
literal|114
block|,
literal|97
block|,
literal|121
block|,
literal|115
block|,
literal|36
block|,
literal|65
block|,
literal|114
block|,
literal|114
block|,
literal|97
block|,
literal|121
block|,
literal|76
block|,
literal|105
block|,
literal|115
block|,
literal|116
block|,
operator|-
literal|39
block|,
operator|-
literal|92
block|,
literal|60
block|,
operator|-
literal|66
block|,
operator|-
literal|51
block|,
operator|-
literal|120
block|,
literal|6
block|,
operator|-
literal|46
block|,
literal|2
block|,
literal|0
block|,
literal|1
block|,
literal|91
block|,
literal|0
block|,
literal|1
block|,
literal|97
block|,
literal|116
block|,
literal|0
block|,
literal|19
block|,
literal|91
block|,
literal|76
block|,
literal|106
block|,
literal|97
block|,
literal|118
block|,
literal|97
block|,
literal|47
block|,
literal|108
block|,
literal|97
block|,
literal|110
block|,
literal|103
block|,
literal|47
block|,
literal|79
block|,
literal|98
block|,
literal|106
block|,
literal|101
block|,
literal|99
block|,
literal|116
block|,
literal|59
block|,
literal|120
block|,
literal|112
block|,
literal|117
block|,
literal|114
block|,
literal|0
block|,
literal|19
block|,
literal|91
block|,
literal|76
block|,
literal|106
block|,
literal|97
block|,
literal|118
block|,
literal|97
block|,
literal|46
block|,
literal|108
block|,
literal|97
block|,
literal|110
block|,
literal|103
block|,
literal|46
block|,
literal|83
block|,
literal|116
block|,
literal|114
block|,
literal|105
block|,
literal|110
block|,
literal|103
block|,
literal|59
block|,
operator|-
literal|83
block|,
operator|-
literal|46
block|,
literal|86
block|,
operator|-
literal|25
block|,
operator|-
literal|23
block|,
literal|29
block|,
literal|123
block|,
literal|71
block|,
literal|2
block|,
literal|0
block|,
literal|0
block|,
literal|120
block|,
literal|112
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|3
block|,
literal|116
block|,
literal|0
block|,
literal|3
block|,
literal|111
block|,
literal|110
block|,
literal|101
block|,
literal|116
block|,
literal|0
block|,
literal|6
block|,
operator|-
literal|48
block|,
operator|-
literal|76
block|,
operator|-
literal|48
block|,
operator|-
literal|78
block|,
operator|-
literal|48
block|,
operator|-
literal|80
block|,
literal|116
block|,
literal|0
block|,
literal|4
block|,
literal|116
block|,
operator|-
literal|59
block|,
operator|-
literal|103
block|,
literal|105
block|}
argument_list|,
name|decoded
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|src
argument_list|,
name|ConsulRegistryUtils
operator|.
name|deserialize
argument_list|(
name|decoded
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

