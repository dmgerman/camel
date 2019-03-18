begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|cdi
package|;
end_package

begin_class
DECL|class|Constants
specifier|public
specifier|final
class|class
name|Constants
block|{
DECL|field|EXPECTED_BODIES_A
specifier|public
specifier|static
specifier|final
name|Object
index|[]
name|EXPECTED_BODIES_A
init|=
block|{
literal|"messageA1"
block|,
literal|"messageA2"
block|}
decl_stmt|;
DECL|field|EXPECTED_BODIES_B
specifier|public
specifier|static
specifier|final
name|Object
index|[]
name|EXPECTED_BODIES_B
init|=
block|{
literal|"messageB1"
block|,
literal|"messageB2"
block|}
decl_stmt|;
DECL|field|EXPECTED_BODIES_C
specifier|public
specifier|static
specifier|final
name|Object
index|[]
name|EXPECTED_BODIES_C
init|=
block|{
literal|"messageC1"
block|,
literal|"messageC2"
block|}
decl_stmt|;
DECL|field|EXPECTED_BODIES_D
specifier|public
specifier|static
specifier|final
name|Object
index|[]
name|EXPECTED_BODIES_D
init|=
block|{
literal|"messageD1"
block|,
literal|"messageD2"
block|}
decl_stmt|;
DECL|field|EXPECTED_BODIES_D_A
specifier|public
specifier|static
specifier|final
name|Object
index|[]
name|EXPECTED_BODIES_D_A
init|=
block|{
literal|"messageDa1"
block|,
literal|"messageDa2"
block|}
decl_stmt|;
DECL|method|Constants ()
specifier|private
name|Constants
parameter_list|()
block|{     }
block|}
end_class

end_unit

