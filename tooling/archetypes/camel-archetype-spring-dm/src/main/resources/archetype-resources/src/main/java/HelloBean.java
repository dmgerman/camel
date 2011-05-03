begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|$
block|{
package|package
block|}
end_package

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * A bean which we use in the route  */
end_comment

begin_class
DECL|class|HelloBean
specifier|public
class|class
name|HelloBean
implements|implements
name|Hello
block|{
DECL|field|say
specifier|private
name|String
name|say
init|=
literal|"Hello World"
decl_stmt|;
DECL|method|hello ()
specifier|public
name|String
name|hello
parameter_list|()
block|{
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss"
argument_list|)
decl_stmt|;
return|return
name|say
operator|+
literal|" at "
operator|+
name|sdf
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getSay ()
specifier|public
name|String
name|getSay
parameter_list|()
block|{
return|return
name|say
return|;
block|}
DECL|method|setSay (String say)
specifier|public
name|void
name|setSay
parameter_list|(
name|String
name|say
parameter_list|)
block|{
name|this
operator|.
name|say
operator|=
name|say
expr_stmt|;
block|}
block|}
end_class

end_unit

