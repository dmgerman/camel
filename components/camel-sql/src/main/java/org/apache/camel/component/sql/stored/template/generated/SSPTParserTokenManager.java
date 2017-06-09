begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/* Generated By:JavaCC: Do not edit this line. SSPTParserTokenManager.java */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored.template.generated
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|generated
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|spi
operator|.
name|ClassResolver
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
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
operator|.
name|*
import|;
end_import

begin_comment
comment|/** Token Manager. */
end_comment

begin_class
DECL|class|SSPTParserTokenManager
specifier|public
class|class
name|SSPTParserTokenManager
implements|implements
name|SSPTParserConstants
block|{
comment|/** Debug output. */
DECL|field|debugStream
specifier|public
name|java
operator|.
name|io
operator|.
name|PrintStream
name|debugStream
init|=
name|System
operator|.
name|out
decl_stmt|;
comment|/** Set debug output. */
DECL|method|setDebugStream (java.io.PrintStream ds)
specifier|public
name|void
name|setDebugStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|PrintStream
name|ds
parameter_list|)
block|{
name|debugStream
operator|=
name|ds
expr_stmt|;
block|}
DECL|method|jjStopStringLiteralDfa_0 (int pos, long active0)
specifier|private
specifier|final
name|int
name|jjStopStringLiteralDfa_0
parameter_list|(
name|int
name|pos
parameter_list|,
name|long
name|active0
parameter_list|)
block|{
switch|switch
condition|(
name|pos
condition|)
block|{
case|case
literal|0
case|:
if|if
condition|(
operator|(
name|active0
operator|&
literal|0x4L
operator|)
operator|!=
literal|0L
condition|)
block|{
name|jjmatchedKind
operator|=
literal|16
expr_stmt|;
return|return
literal|15
return|;
block|}
if|if
condition|(
operator|(
name|active0
operator|&
literal|0x2L
operator|)
operator|!=
literal|0L
condition|)
return|return
literal|25
return|;
return|return
operator|-
literal|1
return|;
case|case
literal|1
case|:
if|if
condition|(
operator|(
name|active0
operator|&
literal|0x4L
operator|)
operator|!=
literal|0L
condition|)
block|{
name|jjmatchedKind
operator|=
literal|16
expr_stmt|;
name|jjmatchedPos
operator|=
literal|1
expr_stmt|;
return|return
literal|15
return|;
block|}
return|return
operator|-
literal|1
return|;
case|case
literal|2
case|:
if|if
condition|(
operator|(
name|active0
operator|&
literal|0x4L
operator|)
operator|!=
literal|0L
condition|)
block|{
name|jjmatchedKind
operator|=
literal|16
expr_stmt|;
name|jjmatchedPos
operator|=
literal|2
expr_stmt|;
return|return
literal|15
return|;
block|}
return|return
operator|-
literal|1
return|;
default|default :
return|return
operator|-
literal|1
return|;
block|}
block|}
DECL|method|jjStartNfa_0 (int pos, long active0)
specifier|private
specifier|final
name|int
name|jjStartNfa_0
parameter_list|(
name|int
name|pos
parameter_list|,
name|long
name|active0
parameter_list|)
block|{
return|return
name|jjMoveNfa_0
argument_list|(
name|jjStopStringLiteralDfa_0
argument_list|(
name|pos
argument_list|,
name|active0
argument_list|)
argument_list|,
name|pos
operator|+
literal|1
argument_list|)
return|;
block|}
DECL|method|jjStopAtPos (int pos, int kind)
specifier|private
name|int
name|jjStopAtPos
parameter_list|(
name|int
name|pos
parameter_list|,
name|int
name|kind
parameter_list|)
block|{
name|jjmatchedKind
operator|=
name|kind
expr_stmt|;
name|jjmatchedPos
operator|=
name|pos
expr_stmt|;
return|return
name|pos
operator|+
literal|1
return|;
block|}
DECL|method|jjMoveStringLiteralDfa0_0 ()
specifier|private
name|int
name|jjMoveStringLiteralDfa0_0
parameter_list|()
block|{
switch|switch
condition|(
name|curChar
condition|)
block|{
case|case
literal|32
case|:
return|return
name|jjStartNfaWithStates_0
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|25
argument_list|)
return|;
case|case
literal|79
case|:
return|return
name|jjMoveStringLiteralDfa1_0
argument_list|(
literal|0x4L
argument_list|)
return|;
default|default :
return|return
name|jjMoveNfa_0
argument_list|(
literal|8
argument_list|,
literal|0
argument_list|)
return|;
block|}
block|}
DECL|method|jjMoveStringLiteralDfa1_0 (long active0)
specifier|private
name|int
name|jjMoveStringLiteralDfa1_0
parameter_list|(
name|long
name|active0
parameter_list|)
block|{
try|try
block|{
name|curChar
operator|=
name|input_stream
operator|.
name|readChar
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|IOException
name|e
parameter_list|)
block|{
name|jjStopStringLiteralDfa_0
argument_list|(
literal|0
argument_list|,
name|active0
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
switch|switch
condition|(
name|curChar
condition|)
block|{
case|case
literal|85
case|:
return|return
name|jjMoveStringLiteralDfa2_0
argument_list|(
name|active0
argument_list|,
literal|0x4L
argument_list|)
return|;
default|default :
break|break;
block|}
return|return
name|jjStartNfa_0
argument_list|(
literal|0
argument_list|,
name|active0
argument_list|)
return|;
block|}
DECL|method|jjMoveStringLiteralDfa2_0 (long old0, long active0)
specifier|private
name|int
name|jjMoveStringLiteralDfa2_0
parameter_list|(
name|long
name|old0
parameter_list|,
name|long
name|active0
parameter_list|)
block|{
if|if
condition|(
operator|(
operator|(
name|active0
operator|&=
name|old0
operator|)
operator|)
operator|==
literal|0L
condition|)
return|return
name|jjStartNfa_0
argument_list|(
literal|0
argument_list|,
name|old0
argument_list|)
return|;
try|try
block|{
name|curChar
operator|=
name|input_stream
operator|.
name|readChar
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|IOException
name|e
parameter_list|)
block|{
name|jjStopStringLiteralDfa_0
argument_list|(
literal|1
argument_list|,
name|active0
argument_list|)
expr_stmt|;
return|return
literal|2
return|;
block|}
switch|switch
condition|(
name|curChar
condition|)
block|{
case|case
literal|84
case|:
return|return
name|jjMoveStringLiteralDfa3_0
argument_list|(
name|active0
argument_list|,
literal|0x4L
argument_list|)
return|;
default|default :
break|break;
block|}
return|return
name|jjStartNfa_0
argument_list|(
literal|1
argument_list|,
name|active0
argument_list|)
return|;
block|}
DECL|method|jjMoveStringLiteralDfa3_0 (long old0, long active0)
specifier|private
name|int
name|jjMoveStringLiteralDfa3_0
parameter_list|(
name|long
name|old0
parameter_list|,
name|long
name|active0
parameter_list|)
block|{
if|if
condition|(
operator|(
operator|(
name|active0
operator|&=
name|old0
operator|)
operator|)
operator|==
literal|0L
condition|)
return|return
name|jjStartNfa_0
argument_list|(
literal|1
argument_list|,
name|old0
argument_list|)
return|;
try|try
block|{
name|curChar
operator|=
name|input_stream
operator|.
name|readChar
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|IOException
name|e
parameter_list|)
block|{
name|jjStopStringLiteralDfa_0
argument_list|(
literal|2
argument_list|,
name|active0
argument_list|)
expr_stmt|;
return|return
literal|3
return|;
block|}
switch|switch
condition|(
name|curChar
condition|)
block|{
case|case
literal|32
case|:
if|if
condition|(
operator|(
name|active0
operator|&
literal|0x4L
operator|)
operator|!=
literal|0L
condition|)
return|return
name|jjStopAtPos
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|)
return|;
break|break;
default|default :
break|break;
block|}
return|return
name|jjStartNfa_0
argument_list|(
literal|2
argument_list|,
name|active0
argument_list|)
return|;
block|}
DECL|method|jjStartNfaWithStates_0 (int pos, int kind, int state)
specifier|private
name|int
name|jjStartNfaWithStates_0
parameter_list|(
name|int
name|pos
parameter_list|,
name|int
name|kind
parameter_list|,
name|int
name|state
parameter_list|)
block|{
name|jjmatchedKind
operator|=
name|kind
expr_stmt|;
name|jjmatchedPos
operator|=
name|pos
expr_stmt|;
try|try
block|{
name|curChar
operator|=
name|input_stream
operator|.
name|readChar
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|IOException
name|e
parameter_list|)
block|{
return|return
name|pos
operator|+
literal|1
return|;
block|}
return|return
name|jjMoveNfa_0
argument_list|(
name|state
argument_list|,
name|pos
operator|+
literal|1
argument_list|)
return|;
block|}
DECL|method|jjMoveNfa_0 (int startState, int curPos)
specifier|private
name|int
name|jjMoveNfa_0
parameter_list|(
name|int
name|startState
parameter_list|,
name|int
name|curPos
parameter_list|)
block|{
name|int
name|startsAt
init|=
literal|0
decl_stmt|;
name|jjnewStateCnt
operator|=
literal|25
expr_stmt|;
name|int
name|i
init|=
literal|1
decl_stmt|;
name|jjstateSet
index|[
literal|0
index|]
operator|=
name|startState
expr_stmt|;
name|int
name|kind
init|=
literal|0x7fffffff
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
if|if
condition|(
operator|++
name|jjround
operator|==
literal|0x7fffffff
condition|)
name|ReInitRounds
argument_list|()
expr_stmt|;
if|if
condition|(
name|curChar
operator|<
literal|64
condition|)
block|{
name|long
name|l
init|=
literal|1L
operator|<<
name|curChar
decl_stmt|;
do|do
block|{
switch|switch
condition|(
name|jjstateSet
index|[
operator|--
name|i
index|]
condition|)
block|{
case|case
literal|8
case|:
if|if
condition|(
operator|(
literal|0x7ff609c00000000L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
block|{
if|if
condition|(
name|kind
operator|>
literal|16
condition|)
name|kind
operator|=
literal|16
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|15
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
block|{
if|if
condition|(
name|kind
operator|>
literal|8
condition|)
name|kind
operator|=
literal|8
expr_stmt|;
name|jjCheckNAddStates
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|curChar
operator|==
literal|40
condition|)
block|{
if|if
condition|(
name|kind
operator|>
literal|11
condition|)
name|kind
operator|=
literal|11
expr_stmt|;
name|jjCheckNAddTwoStates
argument_list|(
literal|17
argument_list|,
literal|19
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|curChar
operator|==
literal|41
condition|)
block|{
if|if
condition|(
name|kind
operator|>
literal|12
condition|)
name|kind
operator|=
literal|12
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|curChar
operator|==
literal|44
condition|)
block|{
if|if
condition|(
name|kind
operator|>
literal|10
condition|)
name|kind
operator|=
literal|10
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
literal|0x3ff200000000000L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
block|{
if|if
condition|(
name|kind
operator|>
literal|4
condition|)
name|kind
operator|=
literal|4
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|curChar
operator|==
literal|39
condition|)
name|jjCheckNAdd
argument_list|(
literal|13
argument_list|)
expr_stmt|;
elseif|else
if|if
condition|(
name|curChar
operator|==
literal|58
condition|)
name|jjstateSet
index|[
name|jjnewStateCnt
operator|++
index|]
operator|=
literal|9
expr_stmt|;
elseif|else
if|if
condition|(
name|curChar
operator|==
literal|36
condition|)
name|jjstateSet
index|[
name|jjnewStateCnt
operator|++
index|]
operator|=
literal|5
expr_stmt|;
break|break;
case|case
literal|25
case|:
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjCheckNAddTwoStates
argument_list|(
literal|24
argument_list|,
literal|3
argument_list|)
expr_stmt|;
elseif|else
if|if
condition|(
name|curChar
operator|==
literal|41
condition|)
block|{
if|if
condition|(
name|kind
operator|>
literal|12
condition|)
name|kind
operator|=
literal|12
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|curChar
operator|==
literal|40
condition|)
block|{
if|if
condition|(
name|kind
operator|>
literal|11
condition|)
name|kind
operator|=
literal|11
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|19
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|curChar
operator|==
literal|44
condition|)
block|{
if|if
condition|(
name|kind
operator|>
literal|10
condition|)
name|kind
operator|=
literal|10
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjCheckNAddTwoStates
argument_list|(
literal|22
argument_list|,
literal|23
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjCheckNAddTwoStates
argument_list|(
literal|21
argument_list|,
literal|1
argument_list|)
expr_stmt|;
break|break;
case|case
literal|0
case|:
if|if
condition|(
operator|(
literal|0x3ff200000000000L
operator|&
name|l
operator|)
operator|==
literal|0L
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|4
condition|)
name|kind
operator|=
literal|4
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
if|if
condition|(
name|curChar
operator|!=
literal|44
condition|)
break|break;
name|kind
operator|=
literal|10
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|2
argument_list|)
expr_stmt|;
break|break;
case|case
literal|2
case|:
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|==
literal|0L
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|10
condition|)
name|kind
operator|=
literal|10
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|2
argument_list|)
expr_stmt|;
break|break;
case|case
literal|3
case|:
if|if
condition|(
name|curChar
operator|!=
literal|41
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|12
condition|)
name|kind
operator|=
literal|12
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|4
argument_list|)
expr_stmt|;
break|break;
case|case
literal|4
case|:
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|==
literal|0L
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|12
condition|)
name|kind
operator|=
literal|12
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|4
argument_list|)
expr_stmt|;
break|break;
case|case
literal|6
case|:
if|if
condition|(
operator|(
literal|0x7ff609d00000000L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjAddStates
argument_list|(
literal|6
argument_list|,
literal|7
argument_list|)
expr_stmt|;
break|break;
case|case
literal|9
case|:
if|if
condition|(
name|curChar
operator|==
literal|35
condition|)
name|jjCheckNAdd
argument_list|(
literal|10
argument_list|)
expr_stmt|;
break|break;
case|case
literal|10
case|:
if|if
condition|(
operator|(
literal|0x7ff609c00000000L
operator|&
name|l
operator|)
operator|==
literal|0L
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|14
condition|)
name|kind
operator|=
literal|14
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|10
argument_list|)
expr_stmt|;
break|break;
case|case
literal|11
case|:
if|if
condition|(
name|curChar
operator|==
literal|58
condition|)
name|jjstateSet
index|[
name|jjnewStateCnt
operator|++
index|]
operator|=
literal|9
expr_stmt|;
break|break;
case|case
literal|12
case|:
if|if
condition|(
name|curChar
operator|==
literal|39
condition|)
name|jjCheckNAdd
argument_list|(
literal|13
argument_list|)
expr_stmt|;
break|break;
case|case
literal|13
case|:
if|if
condition|(
operator|(
literal|0x7ff609c00000000L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjCheckNAddTwoStates
argument_list|(
literal|13
argument_list|,
literal|14
argument_list|)
expr_stmt|;
break|break;
case|case
literal|14
case|:
if|if
condition|(
name|curChar
operator|==
literal|39
operator|&&
name|kind
operator|>
literal|15
condition|)
name|kind
operator|=
literal|15
expr_stmt|;
break|break;
case|case
literal|15
case|:
if|if
condition|(
operator|(
literal|0x7ff609c00000000L
operator|&
name|l
operator|)
operator|==
literal|0L
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|16
condition|)
name|kind
operator|=
literal|16
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|15
argument_list|)
expr_stmt|;
break|break;
case|case
literal|16
case|:
if|if
condition|(
name|curChar
operator|!=
literal|40
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|11
condition|)
name|kind
operator|=
literal|11
expr_stmt|;
name|jjCheckNAddTwoStates
argument_list|(
literal|17
argument_list|,
literal|19
argument_list|)
expr_stmt|;
break|break;
case|case
literal|17
case|:
if|if
condition|(
operator|(
literal|0x3ff200000000000L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjCheckNAddTwoStates
argument_list|(
literal|17
argument_list|,
literal|18
argument_list|)
expr_stmt|;
break|break;
case|case
literal|18
case|:
if|if
condition|(
name|curChar
operator|==
literal|41
operator|&&
name|kind
operator|>
literal|3
condition|)
name|kind
operator|=
literal|3
expr_stmt|;
break|break;
case|case
literal|19
case|:
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|==
literal|0L
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|11
condition|)
name|kind
operator|=
literal|11
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|19
argument_list|)
expr_stmt|;
break|break;
case|case
literal|20
case|:
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|==
literal|0L
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|8
condition|)
name|kind
operator|=
literal|8
expr_stmt|;
name|jjCheckNAddStates
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|)
expr_stmt|;
break|break;
case|case
literal|21
case|:
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjCheckNAddTwoStates
argument_list|(
literal|21
argument_list|,
literal|1
argument_list|)
expr_stmt|;
break|break;
case|case
literal|22
case|:
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjCheckNAddTwoStates
argument_list|(
literal|22
argument_list|,
literal|23
argument_list|)
expr_stmt|;
break|break;
case|case
literal|23
case|:
if|if
condition|(
name|curChar
operator|!=
literal|40
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|11
condition|)
name|kind
operator|=
literal|11
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|19
argument_list|)
expr_stmt|;
break|break;
case|case
literal|24
case|:
if|if
condition|(
operator|(
literal|0x100002600L
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjCheckNAddTwoStates
argument_list|(
literal|24
argument_list|,
literal|3
argument_list|)
expr_stmt|;
break|break;
default|default :
break|break;
block|}
block|}
do|while
condition|(
name|i
operator|!=
name|startsAt
condition|)
do|;
block|}
elseif|else
if|if
condition|(
name|curChar
operator|<
literal|128
condition|)
block|{
name|long
name|l
init|=
literal|1L
operator|<<
operator|(
name|curChar
operator|&
literal|077
operator|)
decl_stmt|;
do|do
block|{
switch|switch
condition|(
name|jjstateSet
index|[
operator|--
name|i
index|]
condition|)
block|{
case|case
literal|8
case|:
case|case
literal|15
case|:
if|if
condition|(
operator|(
literal|0x2ffffffeaffffffeL
operator|&
name|l
operator|)
operator|==
literal|0L
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|16
condition|)
name|kind
operator|=
literal|16
expr_stmt|;
name|jjCheckNAdd
argument_list|(
literal|15
argument_list|)
expr_stmt|;
break|break;
case|case
literal|5
case|:
if|if
condition|(
name|curChar
operator|==
literal|123
condition|)
name|jjCheckNAdd
argument_list|(
literal|6
argument_list|)
expr_stmt|;
break|break;
case|case
literal|6
case|:
if|if
condition|(
operator|(
literal|0x2ffffffeaffffffeL
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjCheckNAddTwoStates
argument_list|(
literal|6
argument_list|,
literal|7
argument_list|)
expr_stmt|;
break|break;
case|case
literal|7
case|:
if|if
condition|(
name|curChar
operator|==
literal|125
operator|&&
name|kind
operator|>
literal|13
condition|)
name|kind
operator|=
literal|13
expr_stmt|;
break|break;
case|case
literal|10
case|:
if|if
condition|(
operator|(
literal|0x2ffffffeaffffffeL
operator|&
name|l
operator|)
operator|==
literal|0L
condition|)
break|break;
if|if
condition|(
name|kind
operator|>
literal|14
condition|)
name|kind
operator|=
literal|14
expr_stmt|;
name|jjstateSet
index|[
name|jjnewStateCnt
operator|++
index|]
operator|=
literal|10
expr_stmt|;
break|break;
case|case
literal|13
case|:
if|if
condition|(
operator|(
literal|0x2ffffffeaffffffeL
operator|&
name|l
operator|)
operator|!=
literal|0L
condition|)
name|jjAddStates
argument_list|(
literal|8
argument_list|,
literal|9
argument_list|)
expr_stmt|;
break|break;
default|default :
break|break;
block|}
block|}
do|while
condition|(
name|i
operator|!=
name|startsAt
condition|)
do|;
block|}
else|else
block|{
name|int
name|i2
init|=
operator|(
name|curChar
operator|&
literal|0xff
operator|)
operator|>>
literal|6
decl_stmt|;
name|long
name|l2
init|=
literal|1L
operator|<<
operator|(
name|curChar
operator|&
literal|077
operator|)
decl_stmt|;
do|do
block|{
switch|switch
condition|(
name|jjstateSet
index|[
operator|--
name|i
index|]
condition|)
block|{
default|default :
break|break;
block|}
block|}
do|while
condition|(
name|i
operator|!=
name|startsAt
condition|)
do|;
block|}
if|if
condition|(
name|kind
operator|!=
literal|0x7fffffff
condition|)
block|{
name|jjmatchedKind
operator|=
name|kind
expr_stmt|;
name|jjmatchedPos
operator|=
name|curPos
expr_stmt|;
name|kind
operator|=
literal|0x7fffffff
expr_stmt|;
block|}
operator|++
name|curPos
expr_stmt|;
if|if
condition|(
operator|(
name|i
operator|=
name|jjnewStateCnt
operator|)
operator|==
operator|(
name|startsAt
operator|=
literal|25
operator|-
operator|(
name|jjnewStateCnt
operator|=
name|startsAt
operator|)
operator|)
condition|)
return|return
name|curPos
return|;
try|try
block|{
name|curChar
operator|=
name|input_stream
operator|.
name|readChar
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|IOException
name|e
parameter_list|)
block|{
return|return
name|curPos
return|;
block|}
block|}
block|}
DECL|field|jjnextStates
specifier|static
specifier|final
name|int
index|[]
name|jjnextStates
init|=
block|{
literal|21
block|,
literal|1
block|,
literal|22
block|,
literal|23
block|,
literal|24
block|,
literal|3
block|,
literal|6
block|,
literal|7
block|,
literal|13
block|,
literal|14
block|,  }
decl_stmt|;
comment|/** Token literal values. */
DECL|field|jjstrLiteralImages
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|jjstrLiteralImages
init|=
block|{
literal|""
block|,
literal|"\40"
block|,
literal|"\117\125\124\40"
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|,
literal|null
block|, }
decl_stmt|;
comment|/** Lexer state names. */
DECL|field|lexStateNames
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|lexStateNames
init|=
block|{
literal|"DEFAULT"
block|, }
decl_stmt|;
DECL|field|input_stream
specifier|protected
name|SimpleCharStream
name|input_stream
decl_stmt|;
DECL|field|jjrounds
specifier|private
specifier|final
name|int
index|[]
name|jjrounds
init|=
operator|new
name|int
index|[
literal|25
index|]
decl_stmt|;
DECL|field|jjstateSet
specifier|private
specifier|final
name|int
index|[]
name|jjstateSet
init|=
operator|new
name|int
index|[
literal|50
index|]
decl_stmt|;
DECL|field|curChar
specifier|protected
name|char
name|curChar
decl_stmt|;
comment|/** Constructor. */
DECL|method|SSPTParserTokenManager (SimpleCharStream stream)
specifier|public
name|SSPTParserTokenManager
parameter_list|(
name|SimpleCharStream
name|stream
parameter_list|)
block|{
if|if
condition|(
name|SimpleCharStream
operator|.
name|staticFlag
condition|)
throw|throw
operator|new
name|Error
argument_list|(
literal|"ERROR: Cannot use a static CharStream class with a non-static lexical analyzer."
argument_list|)
throw|;
name|input_stream
operator|=
name|stream
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SSPTParserTokenManager (SimpleCharStream stream, int lexState)
specifier|public
name|SSPTParserTokenManager
parameter_list|(
name|SimpleCharStream
name|stream
parameter_list|,
name|int
name|lexState
parameter_list|)
block|{
name|this
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|SwitchTo
argument_list|(
name|lexState
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise parser. */
DECL|method|ReInit (SimpleCharStream stream)
specifier|public
name|void
name|ReInit
parameter_list|(
name|SimpleCharStream
name|stream
parameter_list|)
block|{
name|jjmatchedPos
operator|=
name|jjnewStateCnt
operator|=
literal|0
expr_stmt|;
name|curLexState
operator|=
name|defaultLexState
expr_stmt|;
name|input_stream
operator|=
name|stream
expr_stmt|;
name|ReInitRounds
argument_list|()
expr_stmt|;
block|}
DECL|method|ReInitRounds ()
specifier|private
name|void
name|ReInitRounds
parameter_list|()
block|{
name|int
name|i
decl_stmt|;
name|jjround
operator|=
literal|0x80000001
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|25
init|;
name|i
operator|--
operator|>
literal|0
condition|;
control|)
name|jjrounds
index|[
name|i
index|]
operator|=
literal|0x80000000
expr_stmt|;
block|}
comment|/** Reinitialise parser. */
DECL|method|ReInit (SimpleCharStream stream, int lexState)
specifier|public
name|void
name|ReInit
parameter_list|(
name|SimpleCharStream
name|stream
parameter_list|,
name|int
name|lexState
parameter_list|)
block|{
name|ReInit
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|SwitchTo
argument_list|(
name|lexState
argument_list|)
expr_stmt|;
block|}
comment|/** Switch to specified lex state. */
DECL|method|SwitchTo (int lexState)
specifier|public
name|void
name|SwitchTo
parameter_list|(
name|int
name|lexState
parameter_list|)
block|{
if|if
condition|(
name|lexState
operator|>=
literal|1
operator|||
name|lexState
operator|<
literal|0
condition|)
throw|throw
operator|new
name|TokenMgrError
argument_list|(
literal|"Error: Ignoring invalid lexical state : "
operator|+
name|lexState
operator|+
literal|". State unchanged."
argument_list|,
name|TokenMgrError
operator|.
name|INVALID_LEXICAL_STATE
argument_list|)
throw|;
else|else
name|curLexState
operator|=
name|lexState
expr_stmt|;
block|}
DECL|method|jjFillToken ()
specifier|protected
name|Token
name|jjFillToken
parameter_list|()
block|{
specifier|final
name|Token
name|t
decl_stmt|;
specifier|final
name|String
name|curTokenImage
decl_stmt|;
specifier|final
name|int
name|beginLine
decl_stmt|;
specifier|final
name|int
name|endLine
decl_stmt|;
specifier|final
name|int
name|beginColumn
decl_stmt|;
specifier|final
name|int
name|endColumn
decl_stmt|;
name|String
name|im
init|=
name|jjstrLiteralImages
index|[
name|jjmatchedKind
index|]
decl_stmt|;
name|curTokenImage
operator|=
operator|(
name|im
operator|==
literal|null
operator|)
condition|?
name|input_stream
operator|.
name|GetImage
argument_list|()
else|:
name|im
expr_stmt|;
name|beginLine
operator|=
name|input_stream
operator|.
name|getBeginLine
argument_list|()
expr_stmt|;
name|beginColumn
operator|=
name|input_stream
operator|.
name|getBeginColumn
argument_list|()
expr_stmt|;
name|endLine
operator|=
name|input_stream
operator|.
name|getEndLine
argument_list|()
expr_stmt|;
name|endColumn
operator|=
name|input_stream
operator|.
name|getEndColumn
argument_list|()
expr_stmt|;
name|t
operator|=
name|Token
operator|.
name|newToken
argument_list|(
name|jjmatchedKind
argument_list|,
name|curTokenImage
argument_list|)
expr_stmt|;
name|t
operator|.
name|beginLine
operator|=
name|beginLine
expr_stmt|;
name|t
operator|.
name|endLine
operator|=
name|endLine
expr_stmt|;
name|t
operator|.
name|beginColumn
operator|=
name|beginColumn
expr_stmt|;
name|t
operator|.
name|endColumn
operator|=
name|endColumn
expr_stmt|;
return|return
name|t
return|;
block|}
DECL|field|curLexState
name|int
name|curLexState
init|=
literal|0
decl_stmt|;
DECL|field|defaultLexState
name|int
name|defaultLexState
init|=
literal|0
decl_stmt|;
DECL|field|jjnewStateCnt
name|int
name|jjnewStateCnt
decl_stmt|;
DECL|field|jjround
name|int
name|jjround
decl_stmt|;
DECL|field|jjmatchedPos
name|int
name|jjmatchedPos
decl_stmt|;
DECL|field|jjmatchedKind
name|int
name|jjmatchedKind
decl_stmt|;
comment|/** Get the next Token. */
DECL|method|getNextToken ()
specifier|public
name|Token
name|getNextToken
parameter_list|()
block|{
name|Token
name|matchedToken
decl_stmt|;
name|int
name|curPos
init|=
literal|0
decl_stmt|;
name|EOFLoop
label|:
for|for
control|(
init|;
condition|;
control|)
block|{
try|try
block|{
name|curChar
operator|=
name|input_stream
operator|.
name|BeginToken
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|IOException
name|e
parameter_list|)
block|{
name|jjmatchedKind
operator|=
literal|0
expr_stmt|;
name|matchedToken
operator|=
name|jjFillToken
argument_list|()
expr_stmt|;
return|return
name|matchedToken
return|;
block|}
name|jjmatchedKind
operator|=
literal|0x7fffffff
expr_stmt|;
name|jjmatchedPos
operator|=
literal|0
expr_stmt|;
name|curPos
operator|=
name|jjMoveStringLiteralDfa0_0
argument_list|()
expr_stmt|;
if|if
condition|(
name|jjmatchedKind
operator|!=
literal|0x7fffffff
condition|)
block|{
if|if
condition|(
name|jjmatchedPos
operator|+
literal|1
operator|<
name|curPos
condition|)
name|input_stream
operator|.
name|backup
argument_list|(
name|curPos
operator|-
name|jjmatchedPos
operator|-
literal|1
argument_list|)
expr_stmt|;
name|matchedToken
operator|=
name|jjFillToken
argument_list|()
expr_stmt|;
return|return
name|matchedToken
return|;
block|}
name|int
name|error_line
init|=
name|input_stream
operator|.
name|getEndLine
argument_list|()
decl_stmt|;
name|int
name|error_column
init|=
name|input_stream
operator|.
name|getEndColumn
argument_list|()
decl_stmt|;
name|String
name|error_after
init|=
literal|null
decl_stmt|;
name|boolean
name|EOFSeen
init|=
literal|false
decl_stmt|;
try|try
block|{
name|input_stream
operator|.
name|readChar
argument_list|()
expr_stmt|;
name|input_stream
operator|.
name|backup
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|IOException
name|e1
parameter_list|)
block|{
name|EOFSeen
operator|=
literal|true
expr_stmt|;
name|error_after
operator|=
name|curPos
operator|<=
literal|1
condition|?
literal|""
else|:
name|input_stream
operator|.
name|GetImage
argument_list|()
expr_stmt|;
if|if
condition|(
name|curChar
operator|==
literal|'\n'
operator|||
name|curChar
operator|==
literal|'\r'
condition|)
block|{
name|error_line
operator|++
expr_stmt|;
name|error_column
operator|=
literal|0
expr_stmt|;
block|}
else|else
name|error_column
operator|++
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|EOFSeen
condition|)
block|{
name|input_stream
operator|.
name|backup
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|error_after
operator|=
name|curPos
operator|<=
literal|1
condition|?
literal|""
else|:
name|input_stream
operator|.
name|GetImage
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|TokenMgrError
argument_list|(
name|EOFSeen
argument_list|,
name|curLexState
argument_list|,
name|error_line
argument_list|,
name|error_column
argument_list|,
name|error_after
argument_list|,
name|curChar
argument_list|,
name|TokenMgrError
operator|.
name|LEXICAL_ERROR
argument_list|)
throw|;
block|}
block|}
DECL|method|jjCheckNAdd (int state)
specifier|private
name|void
name|jjCheckNAdd
parameter_list|(
name|int
name|state
parameter_list|)
block|{
if|if
condition|(
name|jjrounds
index|[
name|state
index|]
operator|!=
name|jjround
condition|)
block|{
name|jjstateSet
index|[
name|jjnewStateCnt
operator|++
index|]
operator|=
name|state
expr_stmt|;
name|jjrounds
index|[
name|state
index|]
operator|=
name|jjround
expr_stmt|;
block|}
block|}
DECL|method|jjAddStates (int start, int end)
specifier|private
name|void
name|jjAddStates
parameter_list|(
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
do|do
block|{
name|jjstateSet
index|[
name|jjnewStateCnt
operator|++
index|]
operator|=
name|jjnextStates
index|[
name|start
index|]
expr_stmt|;
block|}
do|while
condition|(
name|start
operator|++
operator|!=
name|end
condition|)
do|;
block|}
DECL|method|jjCheckNAddTwoStates (int state1, int state2)
specifier|private
name|void
name|jjCheckNAddTwoStates
parameter_list|(
name|int
name|state1
parameter_list|,
name|int
name|state2
parameter_list|)
block|{
name|jjCheckNAdd
argument_list|(
name|state1
argument_list|)
expr_stmt|;
name|jjCheckNAdd
argument_list|(
name|state2
argument_list|)
expr_stmt|;
block|}
DECL|method|jjCheckNAddStates (int start, int end)
specifier|private
name|void
name|jjCheckNAddStates
parameter_list|(
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
do|do
block|{
name|jjCheckNAdd
argument_list|(
name|jjnextStates
index|[
name|start
index|]
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|start
operator|++
operator|!=
name|end
condition|)
do|;
block|}
block|}
end_class

end_unit

