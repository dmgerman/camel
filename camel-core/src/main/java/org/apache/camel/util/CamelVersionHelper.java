begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * A simple util to test Camel versions.  */
end_comment

begin_class
DECL|class|CamelVersionHelper
specifier|public
specifier|final
class|class
name|CamelVersionHelper
block|{
DECL|method|CamelVersionHelper ()
specifier|private
name|CamelVersionHelper
parameter_list|()
block|{
comment|//utility class, never constructed
block|}
comment|/**      * Checks whether other>= base      *      * @param base  the base version      * @param other the other version      * @return<tt>true</tt> if GE,<tt>false</tt> otherwise      */
DECL|method|isGE (String base, String other)
specifier|public
specifier|static
name|boolean
name|isGE
parameter_list|(
name|String
name|base
parameter_list|,
name|String
name|other
parameter_list|)
block|{
name|String
name|s1
init|=
name|base
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|s2
init|=
name|other
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|""
argument_list|)
decl_stmt|;
comment|// SNAPSHOT as .0
name|s1
operator|=
name|s1
operator|.
name|replace
argument_list|(
literal|"-SNAPSHOT"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|s2
operator|=
name|s2
operator|.
name|replace
argument_list|(
literal|"-SNAPSHOT"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
comment|// then use number comparison
name|int
name|n1
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|s1
argument_list|)
decl_stmt|;
name|int
name|n2
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|s2
argument_list|)
decl_stmt|;
return|return
name|Integer
operator|.
name|compare
argument_list|(
name|n2
argument_list|,
name|n1
argument_list|)
operator|>=
literal|0
return|;
block|}
block|}
end_class

end_unit

