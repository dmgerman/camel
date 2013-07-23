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
comment|/**  * Unit utils.  */
end_comment

begin_class
DECL|class|UnitUtils
specifier|public
specifier|final
class|class
name|UnitUtils
block|{
DECL|method|UnitUtils ()
specifier|private
name|UnitUtils
parameter_list|()
block|{     }
comment|/**      * If having a size in bytes and wanting to print this in human friendly\      * format with xx kB, xx MB, xx GB instead of a large byte number.      *      * @param bytes  the value in bytes      */
DECL|method|printUnitFromBytes (long bytes)
specifier|public
specifier|static
name|String
name|printUnitFromBytes
parameter_list|(
name|long
name|bytes
parameter_list|)
block|{
comment|// http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
name|int
name|unit
init|=
literal|1000
decl_stmt|;
if|if
condition|(
name|bytes
operator|<
name|unit
condition|)
block|{
return|return
name|bytes
operator|+
literal|" B"
return|;
block|}
name|int
name|exp
init|=
call|(
name|int
call|)
argument_list|(
name|Math
operator|.
name|log
argument_list|(
name|bytes
argument_list|)
operator|/
name|Math
operator|.
name|log
argument_list|(
name|unit
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|pre
init|=
literal|""
operator|+
literal|"kMGTPE"
operator|.
name|charAt
argument_list|(
name|exp
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%.1f %sB"
argument_list|,
name|bytes
operator|/
name|Math
operator|.
name|pow
argument_list|(
name|unit
argument_list|,
name|exp
argument_list|)
argument_list|,
name|pre
argument_list|)
return|;
block|}
block|}
end_class

end_unit

